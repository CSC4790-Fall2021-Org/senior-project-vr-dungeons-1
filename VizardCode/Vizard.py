import viz
import vizact
import vizshape
import vizconnect
import vizfx
from vizconnect.util import view_collision
import csv
import random
import math

viz.fov(90)
view = viz.MainView

#=====================================
#Position the view of the camera

#CAVE specific:
#CONFIG_FILE = "E:\\VizardProjects\\_CaveConfigFiles\\vizconnect_config_CaveFloor+ART_headnode.py"
#vizconnect.go(CONFIG_FILE)
#viz.clearcolor(viz.WHITE)
#viewPoint = vizconnect.addViewpoint(pos=[1,1,-7])
#viewPoint.add(vizconnect.getDisplay())
#vizconnect.resetViewpoints()

################################################################
#p1 and p2 are points, each is an array of [x,y,z]

#column & row number where the entrance is
startColumn = 0
startRow = 0

def IsThisVillanovaCAVE():
	cave_host_names = ["exx-PC","render-PC"]
	import socket
	if socket.gethostname() in cave_host_names:
		return True
	else:
		return False
	


viz.phys.enable()
#vizshape.addGrid(color=[0.2]*3).setPosition([0.5,1,0.5])

#Changes how lighting works around the main view
myLight = viz.MainView.getHeadLight()
myLight.color(viz.ORANGE)
myLight.intensity(0.5)
vizact.onkeydown('f', myLight.enable)
vizact.onkeydown('g', myLight.disable)

tex1 = viz.addTexture("stonewall.png")

northTex = tex1
southTex = tex1
eastTex = tex1
westTex = tex1

ladder = viz.addChild("stairsRedux.fbx")
ladder.scale(.005,.0038,.005)

scale = 1

light = True 

#Create the master Light Orb to copy to different places around the map
orbLight = vizfx.addPointLight(pos=(0,2,0), color=viz.ORANGE)
orbLight.intensity(2)
sphere = vizshape.addSphere(radius=0.5,pos=(0,-5,0),lighting=light)
sphere.visible(viz.OFF)
viz.link(sphere, orbLight)

#creates the master floor tile from which every other tile will be cloned and sets it at position [1,-1,0], underneath the floor
floor = vizshape.addQuad(size=(scale*1.0,scale*1.0),axis=vizshape.AXIS_Y,texture=tex1,lighting=light)
floor.setPosition([0,-5,0])
floor.collidePlane()

#creates the master north south east and west wall tiles from which every other wall tile will be cloned and sets their position at [1,-1,0], underneath the floor
north = vizshape.addQuad(size=(scale*1.0,scale*5),axis=-vizshape.AXIS_Z,texture=northTex,lighting=light)
north.setPosition([0,-4.5,0.5])

south = vizshape.addQuad(size=(scale*1.0,scale*5),axis=vizshape.AXIS_Z,texture=southTex,lighting=light)
south.setPosition([0,-4.5,-0.5])

east = vizshape.addQuad(size=(scale*1.0,scale*5),axis=-vizshape.AXIS_X,texture=eastTex,lighting=light)
east.setPosition([0.5,-4.5,0])

west = vizshape.addQuad(size=(scale*1.0,scale*5),axis=vizshape.AXIS_X,texture=westTex,lighting=light)
west.setPosition([-0.5,-4.5,0])



#reads from the csv file in GeneratorCode rechange to open('../outputCellAutoHallways.csv)
with open('../GeneratorCode/dungeonCSV/output.csv') as csv_file:
	reader = csv.reader(csv_file, delimiter=',')
	data = list(reader)[0]
	


layout = []

#takes the first two numbers from the csv, which contain the width and height of the 2d dungeon
width = int(data.pop(0))
height = int(data.pop(0))


print("width = ", width)
print("height = ", height)

firstX = int(data.pop(0))
firstY = int(data.pop(0))

#row will be used to count the rows, starting at 0 with the first row+=1
row = -1
for i in range(0,len(data)):
	#if we've reached the beginning of a new row, add a new list to the list
	if(i%width == 0):
		layout.append([])
		row+=1
	#add the data from the csv to the current row
	layout[row].append(data[i])

#row and col temp variables for counting, starting at 0 with the first row+=1 and col+=1
row = -1
col = -1

#iterate over every entry in the 2d list
for r in range(0,height-1):
	for c in range(0,width-1):
		entry = layout[r][c]
		#if there should be a floor at (row,col), clone the master floor to (row,1,col)
		if(entry=="false"):
			floor.copy().setPosition(scale*r,0,scale*c)
			#if(row == 0 or col == 0): #finds the empty space in the first row, the entrance
				#viz.MainView.setPosition([row+3.5,col+2.8, 0])
				
			##if there should be a wall on the left (west)
			if((r==0 or layout[r-1][c]=="true")):
				west.copy().setPosition(scale*(r-0.5),1.5,scale*c)
			##if there should be a wall on the right (east)
			if((r==width-1 or layout[r+1][c]=="true")):
				east.copy().setPosition(scale*(r+0.5),1.5,scale*c)
			##if there should be a wall on the top (north)
			if((c==0 or layout[r][c+1]=="true")):
				north.copy().setPosition(scale*r,1.5,scale*(c+0.5))
				copysphere = sphere.copy()
				copysphere.setPosition(scale*r,1.5,scale*(c+0.5))
				copysphere.visible(viz.OFF)
			##if there should be a wall on the bottom (south)
			if((c==height-1 or layout[r][c-1]=="true")):
				south.copy().setPosition(scale*r,1.5,scale*(c-0.5))
				#print(row)
				#print(col)
		#else:
			#wall.copy().setPosition(scale*row,1.5,scale*col)
			#wall.copy().setPosition(scale*row,1.5+scale*1.0,scale*col)
			#wall.copy().setPosition(scale*row,1.5+scale*2.0,scale*col)
#generate roof
ceiling = vizshape.addQuad(size=(scale*1.0,scale*1.0),axis=vizshape.AXIS_Y,texture=tex1,lighting=light)
ceiling.setPosition([0,-6,0])
#set stair positions
temp = True
'''while temp == True:
	xCor = random.randint(0,width-1)
	zCor = random.randint(0,height-1)
	if layout[xCor][zCor] == "false":
		temp = False
'''

#Kevin remove this later
xCor = firstX
zCor = firstY+2


for i in range(0,height-1):
	for j in range(0,width-1):
		if layout[i][j] == "false" and (i != xCor or j != zCor):
			ceiling.copy().setPosition([i,4,j]) #probably just continuing through the if statement not for loop
		
ladder.setPosition(xCor,0, zCor)


# Ghost code
UPDATE_RATE = 0
#zChange = 0

ghost = viz.addChild("Ghost.fbx")
ghost.scale(0.0007,0.0007,0.0007)
ghost.setPosition(xCor+4,2, zCor+1)
ghost.color( viz.GREEN )

def rotateGhost():
	
	vPos = view.getPosition()
	vPosX = vPos[0]
	vPosZ = vPos[2]
	
	gPos = ghost.getPosition()
	gPosX = gPos[0]
	gPosZ = gPos[2]
	
	dX = gPosX-vPosX
	dZ = gPosZ-vPosZ
	ghostRad = math.atan( dX/dZ ) # angle in radians
	ghostDir = math.atan( dX/dZ ) * 180/ math.pi # angle in degrees
	
	"""
	ghost.setAxisAngle([0,1,0,ghostDir], viz.ABS_GLOBAL)
	ghost.setPosition([0.01,0,0], viz.REL_LOCAL)
	"""
	
	#print(dX, dZ)
	
	# rotates ghost to face player
	if(dZ < 0):
		ghostDir = ghostDir + 180
		ghostRad = ghostRad + viz.radians(180)

	ghost.setEuler([ghostDir,0,0])
	xMod = math.sin(ghostRad) * 0.1
	zMod = math.cos(ghostRad) * 0.1
		
	#ghost.setAxisAngle([0,ghostDir,0,0], viz.ABS_GLOBAL)
	
	#ghostRad equals viz.radians(ghostDir)

	print("dir", ghostDir, "gXZ [", gPosX, gPosZ, "] - vXZ[", vPosX,vPosZ, "] = dXZ [", dX, dZ, "] ; xzMod [", xMod, zMod, "]")
	#print("theta",ghostRad, "gXZ", gPosX, gPosZ, "vXZ", vPosX,vPosZ, "xMod", xMod, "zMod", zMod)
	
	gposX = gPosX - xMod
	gPosZ = gPosZ - zMod
	#print("theta",ghostRad, gPosX, "+", xMod, "=", xMod)
	#print(xMod, zMod)
	#gPosX = gPosX + 0.01
	#ghost.setPosition(gPosX, 2, gPosZ)
	
#setup a timer and specify it's rate and the function to call
vizact.ontimer(UPDATE_RATE, rotateGhost)

#math.sin(viz.radians(angle))

"""
#The speed of the timer.  A value of 0 means the timer function
#Will be called every frame
UPDATE_RATE = 0

#A variable to hold the angle
angle = 0

#Add a model to rotate
h = viz.addChild('tut_hedra.wrl')

#Place the model in front of the viewer
h.setPosition([0,0,6])

def moveModel():
	global angle

	#Increment the angle based on elapsed time
	angle = angle + (SPEED * viz.elapsed())

	#Update the models rotation
	h.setEuler([angle,0,0])

#setup a timer and specify it's rate and the function to call
vizact.ontimer(UPDATE_RATE, moveModel)
"""



#ladder.setPosition(firstX+8,0,firstY)
viz.MainView.stepsize(4)
#create second floor
'''
with open('../GeneratorCode/dungeonCSV/outputDemo.csv') as csv_file:
	reader2 = csv.reader(csv_file, delimiter=',')
	data2 = list(reader2)[0]
	
layout2 = []

#takes the first two numbers from the csv, which contain the width and height of the 2d dungeon
width2 = int(data2.pop(0))
height2 = int(data2.pop(0))

print("width = ", width2)
print("height = ", height2)

firstX2 = int(data2.pop(0))
firstY2 = int(data2.pop(0))

#row will be used to count the rows, starting at 0 with the first row+=1
row2 = -1
for k in range(0,len(data2)):
	#if we've reached the beginning of a new row, add a new list to the list
	if(k%width2 == 0):
		layout2.append([])
		row2+=1
	#add the data from the csv to the current row
	layout2[row2].append(data2[k])

#row and col temp variables for counting, starting at 0 with the first row+=1 and col+=1
row2 = -1
col2 = -1
'''
"""
#iterate over every entry in the 2d list
for r2 in range(0,height2-1):
	for c2 in range(0,width2-1):
		entry2 = layout2[r2][c2]
		#if there should be a floor at (row,col), clone the master floor to (row,1,col)
		if(entry2=="false"):
			floor.copy().setPosition(scale*r,3,scale*c)
			
			#if(row == 0 or col == 0): #finds the empty space in the first row, the entrance
				#viz.MainView.setPosition([row+3.5,col+2.8, 0])
			
			##if there should be a wall on the left (west)
			if((r2==0 or layout[r2-1][c2]=="true")):
				west.copy().setPosition(scale*(r2-0.5),4.5,scale*c2)
			##if there should be a wall on the right (east)
			if((r2==width2-1 or layout2[r2+1][c2]=="true")):
				east.copy().setPosition(scale*(r2+0.5),4.5,scale*c2)
			##if there should be a wall on the top (north)
			if((c2==0 or layout2[r2][c2+1]=="true")):
				north.copy().setPosition(scale*r,4.5,scale*(c2+0.5))
			##if there should be a wall on the bottom (south)
			if((c2==height2-1 or layout2[r2][c2-1]=="true")):
				south.copy().setPosition(scale*r2,4.5,scale*(c2-0.5))
				#print(row)
				#print(col)
		#else:
			#wall.copy().setPosition(scale*row,1.5,scale*col)
			#wall.copy().setPosition(scale*row,1.5+scale*1.0,scale*col)
			#wall.copy().setPosition(scale*row,1.5+scale*2.0,scale*col)
"""
view.setPosition([firstX*scale,0.5,firstY*scale])

#if not IsThisVillanovaCAVE():
#	viz.MainView.setPosition([startColumn+3.5,2.8,2.8])
#	print("made it here")
#	#sets the start position to 10 feet behind the entrance	
	
viz.MainView.collision(viz.ON)
	
#example:
if IsThisVillanovaCAVE():
	#  =====================================
	#Position the view of the camera
	#CAVE specific:
	CONFIG_FILE = "E:\\VizardProjects\\_CaveConfigFiles\\vizconnect_config_CaveFloor+ART_headnode.py"
	vizconnect.go(CONFIG_FILE)
	viewPoint = vizconnect.addViewpoint(pos=[firstX*scale,1,firstY*scale])
	viewPoint.add(vizconnect.getDisplay())
	vizconnect.resetViewpoints()
	
	testPosition = [ 0.677198, 0.000000, 0.735801, 0.000000, 0.000000, 1.000000, -0.000000, 0.000000, -0.735801, -0.000000, 0.677198, 0.000000, 0.519656, -0.579802, -0.446693, 1.000000 ]
	vizconnect.getTransport('wandmagiccarpet').getNode3d().setMatrix(testPosition)
###############################################################
#p1 and p2 are points, each is an array of [x,y,z]
else:
	viz.go()	
	view.setPosition([firstX*scale,1,firstY*scale])
		
	#boilerplate for my local laptop	

#sphere = vizshape.addSphere(radius=1.0,pos=(firstX*scale,0,firstY*scale),lighting=False)
#sphere.color(viz.WHITE)



# Create directional lights
light1 = vizfx.addDirectionalLight(euler=(40,20,0), color=[0.7,0.7,0.7])
light2 = vizfx.addDirectionalLight(euler=(-65,15,0), color=[0.5,0.25,0.0])
# Adjust ambient color
vizfx.setAmbientColor([0.3,0.3,0.4])


print("Done")
print("firstX = ", firstX)
print("firstY = ", firstY)
print("xCor = ",xCor)
print("zCor = ",zCor)
print("getposition = ",view.getPosition())



