"""
This script demonstrates how to use WebSockets with Vizard.
WebSockets allow bidirectional communication with external browsers.
This example displays an overhead view of the world with the current
view position overlayed. The browser client can click on the map
to move the viewpoint and can disorient the user and send messages.

Enter the following URL in your browser to communicate with the script:
{}
"""
import viz
import vizact
import vizcam
import vizmat
import vizdlg
import viztask
import vizhtml

import base64
import random

viz.setMultiSample(8)
viz.fov(60)
viz.go()

# Add environment
model = viz.addChild('maze.osgb')

# Initialize walk navigation
vizcam.WalkNavigate()

# Base 64 encoded png buffer of overhead image
overheadImage = None

# Overhead view range
ORTHO_RANGE = [-13,13]

# Overhead image size
IMAGE_SIZE = [512,512]

def CreateOverHeadImageTask():
	global overheadImage

	# Render overhead scene to texture
	texture = viz.addRenderTexture()
	cam = viz.addRenderNode(size=IMAGE_SIZE)
	cam.setInheritView(False)
	cam.setRenderTexture(texture)
	cam.setPosition([0,5,0])
	cam.setEuler([0,90,0])
	min_ortho, max_ortho = ORTHO_RANGE
	cam.setProjectionMatrix(viz.Matrix.ortho(min_ortho,max_ortho,min_ortho,max_ortho,0.1,100))
	cam.setAutoClip(False)

	# Wait a frame to allow texture to render
	yield None

	# Save texture to buffer base64 encoded png buffer
	overheadImage = base64.b64encode(texture.saveToBuffer('.png'))

	# Send image to existing clients
	vizhtml.sendAll('set_image',viz.Data(data=overheadImage))

	# Remove resources
	cam.remove()
	texture.remove()

viztask.schedule( CreateOverHeadImageTask() )

def PixelToWorld(pos):
	"""Converts overhead image pixel coordinates to world coordinates"""
	px, py = pos
	x = vizmat.Interpolate(ORTHO_RANGE[0],ORTHO_RANGE[1],viz.clamp(float(px)/IMAGE_SIZE[0],0.0,1.0))
	y = vizmat.Interpolate(ORTHO_RANGE[0],ORTHO_RANGE[1],viz.clamp(float(py)/IMAGE_SIZE[1],0.0,1.0))
	return [x,y]

def WorldToPixel(pos):
	"""Converts world coordinates to overhead image pixel coordinates"""
	x,y = pos
	px = viz.clamp(vizmat.InverseInterpolate(ORTHO_RANGE[0],ORTHO_RANGE[1],x),0.0,1.0)
	py = viz.clamp(vizmat.InverseInterpolate(ORTHO_RANGE[0],ORTHO_RANGE[1],y),0.0,1.0)
	px = int(round(px*(IMAGE_SIZE[0]-1)))
	py = int(round(py*(IMAGE_SIZE[1]-1)))
	return [px,py]

def ClientConnect(e):
	"""Send overhead image when new client connects"""
	if overheadImage is not None:
		e.client.send('set_image',viz.Data(data=overheadImage))
vizhtml.onConnect(ClientConnect)

def ClientSetView(e):
	"""Set view position to specified overhead image pixel coordinates"""

	# Animate view to new position
	x,y = PixelToWorld(e.data.pos)
	viz.MainView.runAction(vizact.moveTo([x,1.8,y],time=0.5,interpolate=vizact.easeInOutStrong))

vizhtml.onMessage('set_view',ClientSetView)

def ClientDisorient(e):
	"""Disorient user by spinning around by random amount"""
	amount = random.uniform(1000.0,2000.0)
	euler = viz.MainView.getEuler()
	yaw_spin = vizact.mix(euler[0],euler[0]+amount,time=1.0,interpolate=vizact.easeOutStrong)
	viz.MainView.runAction(vizact.call(viz.MainView.setEuler,yaw_spin,euler[1],euler[2]),pool=1)

vizhtml.onMessage('disorient',ClientDisorient)

# Create panel for displaying messages
messagePanel = vizdlg.Panel(layout=vizdlg.LAYOUT_VERT_RIGHT,align=viz.ALIGN_RIGHT_TOP,border=False,background=False)
messagePanel.visible(False)
viz.link(viz.MainWindow.RightTop,messagePanel,offset=(-10,-10,0))

def DisplayMessageTask(message):
	"""Displays a message for a limited amount of time"""

	# Add message to panel
	text = vizdlg.Panel()
	text.addItem(viz.addText(message))
	messagePanel.addItem(text)
	messagePanel.visible(True)

	# Wait for a few seconds
	yield viztask.waitTime(5.0)

	# Remove message
	messagePanel.removeItem(text)
	if not messagePanel.getItems():
		messagePanel.visible(False)

def ClientTextMessage(e):
	if e.data.message:
		viztask.schedule(DisplayMessageTask(e.data.message))
vizhtml.onMessage('send_message',ClientTextMessage)

def SendView():
	"""Send current view position to all connected clients"""

	# Get position in pixel units
	x,y,z = viz.MainView.getPosition()
	pos = WorldToPixel([x,z])

	# Get rotation
	rotation = int(round(viz.MainView.getEuler()[0]))

	vizhtml.sendAll('set_view',viz.Data(pos=pos,rotation=rotation))

vizact.ontimer(0,SendView)

# HTML code
code = """
<!DOCTYPE html>
<html>
<head>
	<title>vizhtml WebSocket Example</title>
	<script type="text/javascript" src="jquery.js"></script>
	<script type="text/javascript" src="vizhtml.js"></script>
	<style type="text/css">
	.overlay
	{
		position : absolute;
		visibility : hidden;
		z-index : 2;
	}
	</style>
</head>
<body>
<script language="javascript">

$(document).ready(function() {
	if(viz.isWebSocketSupported()) {

		var socket = new viz.WebSocket();

		socket.onevent('open', function(e){
			document.getElementById('status').innerHTML = 'connected';
			$('#input_controls :input').prop('disabled', false);
		})

		socket.onevent('close', function(e){
			document.getElementById('status').innerHTML = 'waiting for connection...';
			$('#input_controls :input').prop('disabled', true);
			document.getElementById('crosshair').style.visibility = 'hidden';
			socket.reconnect();
		})

		socket.onevent('set_image', function(e){
			document.getElementById('screen').src = 'data:image/png;base64,' + e.data.data;
		})

		socket.onevent('set_view', function(e){
			var screen = document.getElementById('screen');
			var crosshair = document.getElementById('crosshair');
			var pos_x = screen.offsetLeft + e.data.pos[0] - (crosshair.width / 2.0);
			var pos_y = screen.offsetTop + (screen.height - e.data.pos[1] - 1) - (crosshair.height / 2.0);
			crosshair.style.left = pos_x + "px";
			crosshair.style.top = pos_y + "px";
			crosshair.style.webkitTransform = "rotate(" + e.data.rotation + "deg)";
			crosshair.style.MozTransform = "rotate(" + e.data.rotation + "deg)";
			crosshair.style.transform = "rotate(" + e.data.rotation + "deg)";
			crosshair.style.visibility = 'visible';
		})

		$("#disorient").click(function() {
			socket.send( 'disorient' , viz.Data({}) );
		});

		function sendMessage()
		{
			var message = document.getElementById('message');
			socket.send('send_message', viz.Data({'message':message.value}) );
			message.value = "";
		}

		$("#send_message").click(function() {
			sendMessage();
		});

		$("#message").keyup(function(event) {
			if(event.keyCode == 13) {
				sendMessage();
			}
		});

		$("#screen").click(function(e) {
			if(socket.isConnected()) {
				var screen = document.getElementById('screen');
				var pos_x = e.offsetX?(e.offsetX):e.pageX-screen.offsetLeft;
				var pos_y = e.offsetY?(e.offsetY):e.pageY-screen.offsetTop;
				pos_y = screen.height - pos_y - 1;
				socket.send( 'set_view' , viz.Data({'pos':[pos_x,pos_y]}) );
			}
		});

	} else {
		$('#content').html('<h3>Your browser does not support WebSockets</h3>');
	}
});

</script>
<div id="content">
	<div>Status:</div><div id='status'>waiting for connection</div></br>
	<div>Screen (click on image to move user):</div>
	<img id="screen" ondragstart="return false;" /></br>
	<img id="crosshair" src="images/red_arrow.png" class="overlay" />
	</br>
	<div id='input_controls'>
		<input id='disorient' type=button value="Disorient User"></br>
		</br>
		Message:</br>
		<input id="message" type="text" value=""> <input id="send_message" type="button" value="Send">
	</div>
</div>
</body>
</html>
"""

# http://localhost:8080/vizhtml/websocket
vizhtml.registerCode('websocket',code, directory='')

# Insert URL in doc string and display in info panel
import vizinfo
vizinfo.InfoPanel(__doc__.format(vizhtml.getServerURL('websocket')))
