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

path = ""
dunType = -1 #change to the type you want, leave -1 for random (1 = BRW, 2 = CA, 3 = Voronoi)
dunNum = -1 #change to the number you want, leave -1 for random

if(dunType == -1):
	dunType = random.randint(1,3)
if(dunNum == -1):
	dunNum = random.randint(0,49)

if(dunType == 1):
	path = "BRW" + str(dunNum)
elif(dunType == 2):
	path = "CA" + str(dunNum)
elif(dunType == 3):
	path = "Voronoi" + str(dunNum)
	
path = path + ".csv"
	
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
		print("conneced to CAVE");
		return True
	else:
		return False
	


#viz.phys.enable()
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
north.collidePlane()

south = vizshape.addQuad(size=(scale*1.0,scale*5),axis=vizshape.AXIS_Z,texture=southTex,lighting=light)
south.setPosition([0,-4.5,-0.5])
south.collidePlane()

east = vizshape.addQuad(size=(scale*1.0,scale*5),axis=-vizshape.AXIS_X,texture=eastTex,lighting=light)
east.setPosition([0.5,-4.5,0])
east.collidePlane()

west = vizshape.addQuad(size=(scale*1.0,scale*5),axis=vizshape.AXIS_X,texture=westTex,lighting=light)
west.setPosition([-0.5,-4.5,0])
west.collidePlane()



#reads from the csv file in GeneratorCode/dungeonCSV/
#'../GeneratorCode/dungeonCSV/output.csv'
with open('../GeneratorCode/dungeonCSV/demoCSVs/'+path) as csv_file:
	reader = csv.reader(csv_file, delimiter=',')
	data = list(reader)[0]
	


layout = []

#takes the first two numbers from the csv, which contain the width and height of the 2d dungeon
width = int(data.pop(0))
height = int(data.pop(0))


print(("width = ", width))
print(("height = ", height))

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
for r in range(0,height):
	for c in range(0,width):
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
			if((r==height-1 or layout[r+1][c]=="true")):
				east.copy().setPosition(scale*(r+0.5),1.5,scale*c)
			##if there should be a wall on the top (north)
			if((c==width-1 or layout[r][c+1]=="true")):
				north.copy().setPosition(scale*r,1.5,scale*(c+0.5))
				copysphere = sphere.copy()
				copysphere.setPosition(scale*r,1.5,scale*(c+0.5))
				copysphere.visible(viz.OFF)
			##if there should be a wall on the bottom (south)
			if((c==0 or layout[r][c-1]=="true")):
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
validPos = True
'''while validPos == True:
	xCor = random.randint(0,width-1)
	zCor = random.randint(0,height-1)
	if layout[xCor][zCor] == "false":
		validPos = False
'''

#Kevin remove this later
xCor = firstX
zCor = firstY+2


for i in range(0,height):
	for j in range(0,width):
		if layout[i][j] == "false" and (i != xCor or j != zCor):
			ceiling.copy().setPosition([i,4,j]) #probably just continuing through the if statement not for loop
		
ladder.setPosition(xCor,0, zCor)


def resetGame():
	print("Resetting game")
	spawnPlayer()
	spawnGhost()


# Ghost code
ghost = viz.addChild("Ghost.fbx")
ghost.scale(0.0007,0.0007,0.0007)
ghost.color( viz.GREEN )

	
# spawn Player
def spawnPlayer():
	#view.setPosition([firstX*scale,0.5,firstY*scale])
	
	validPos = True
	while validPos == True:
		vX = random.randint(0,width-1)
		vZ = random.randint(0,height-1)
		if layout[vX][vZ] == "false":
			validPos = False
	view.setPosition(vX,0.5,vZ)
	print((str(view.getPosition()) + ": player position"))
	print("Player spawned")
spawnPlayer()

# takes Ghost x,z as input, outpu
def calculateDistance(gPosX, gPosZ):
	vPos = view.getPosition()
	vPosX = vPos[0]
	vPosZ = vPos[2]
	
	dX = gPosX-vPosX
	dZ = gPosZ-vPosZ
	
	# ghost distance
	dist = math.sqrt( dX*dX + dZ*dZ )
	#print ("distance: " + str(dist))
	return dist
	
def spawnGhost():
	global ghost
	
	# randomly position ghost
	distance = 0
	while distance < 50:
		gX = int(random.randint(0,width-1)) # why is this casted?
		gZ = random.randint(0,height-1)
		distance = calculateDistance(gX, gZ)
	ghost.setPosition(gX,2,gZ)
	print((str(ghost.getPosition()) +  ": ghost position"))
	print("Ghost spawned")
	#viz.MainView.setScene(viz.Scene1)
spawnGhost()

GHOST_SPEED = 0.1 # reset to 0.02
	
fadeAction = vizact.fadeTo(viz.BLACK, time = 2)

	
# this loop runs until Vizard exits
def moveGhost():
	
	global ghost
	
	# get ghost and viewer positions
	vPos = view.getPosition()
	vPosX = vPos[0]
	vPosZ = vPos[2]
	
	gPos = ghost.getPosition()
	gPosX = gPos[0]
	gPosZ = gPos[2]
	
	dX = gPosX-vPosX
	dZ = gPosZ-vPosZ
	
	# ghost distance
	dist = math.sqrt( dX*dX + dZ*dZ )
	
	if(dist < 0.60):
		#viz.MainView.setScene(viz.Scene2)
		print("got you!")
		
		distance = 0
		while distance < 50:
			gPosX = int(random.randint(0,width-1)) # why is this casted?
			gPosZ = random.randint(0,height-1)
			distance = calculateDistance(gPosX, gPosZ)
		ghost.setPosition(gPosX,2,gPosZ)
		
		print((str(ghost.getPosition()) +  ": ghost position"))
		print("Ghost spawned")
	
	# proximity warnings
	elif(dist < height/8):
		print(("Here he comes!!!", round(dist,2)))
	elif(dist < height/6):
		print(("He's almost got you!!", round(dist,2)))
	elif(dist < height/3):
		print(("He's coming!", round(dist,2)))
	
	
	# rotates ghost to face player
	ghostDir = math.atan( dX/dZ ) * 180/ math.pi # angle in degrees
	if(dZ < 0):
		ghostDir = ghostDir + 180
	ghost.setEuler( [ghostDir,0,0] )
	
	# calculate new ghost position
	xMod = math.sin( viz.radians(ghostDir) ) * GHOST_SPEED
	zMod = math.cos( viz.radians(ghostDir) ) * GHOST_SPEED
	
	ghost.setPosition(gPosX - xMod, 2, gPosZ - zMod)
	
#setup a timer and specify it's rate and the function to call
UPDATE_RATE = 0
vizact.ontimer(UPDATE_RATE, moveGhost)


#ladder.setPosition(firstX+8,0,firstY)
viz.MainView.stepsize(4)


#if not IsThisVillanovaCAVE():
#	viz.MainView.setPosition([startColumn+3.5,2.8,2.8])
#	print("made it here")
#	#sets the start position to 10 feet behind the entrance	
	

	
#example:
if IsThisVillanovaCAVE():
	#  =====================================
	#Position the view of the camera
	#CAVE specific:
	CONFIG_FILE = "E:\\VizardProjects\\_CaveConfigFiles\\vizconnect_config_CaveFloor+ART_headnode.py"
	vizconnect.go(CONFIG_FILE)
	viewPoint = vizconnect.addViewpoint(pos=[firstX*scale,1.2,firstY*scale])
	viewPoint.add(vizconnect.getDisplay())
	vizconnect.resetViewpoints()
	viz.MainView.collision(viz.ON)
	
	testPosition = [ 0.677198, 0.000000, 0.735801, 0.000000, 0.000000, 1.000000, -0.000000, 0.000000, -0.735801, -0.000000, 0.677198, 0.000000, 0.519656, -0.579802, -0.446693, 1.000000 ]
	vizconnect.getTransport('wandmagiccarpet').getNode3d().setMatrix(testPosition)
###############################################################
#p1 and p2 are points, each is an array of [x,y,z]
else:
	viz.MainView.collision(viz.ON)
	viz.go()	
	view.setPosition([firstX*scale,1.2,firstY*scale])
		
	#boilerplate for my local laptop	

#sphere = vizshape.addSphere(radius=1.0,pos=(firstX*scale,0,firstY*scale),lighting=False)
#sphere.color(viz.WHITE)


# Create directional lights
light1 = vizfx.addDirectionalLight(euler=(40,20,0), color=[0.7,0.7,0.7])
light2 = vizfx.addDirectionalLight(euler=(-65,15,0), color=[0.5,0.25,0.0])
# Adjust ambient color
vizfx.setAmbientColor([0.3,0.3,0.4])

print("Done")

print(("firstX = ", firstX))
print(("firstY = ", firstY))
print(("xCor = ",xCor))
print(("zCor = ",zCor))
print(("getposition = ",view.getPosition()))

print("opening file ", path)
