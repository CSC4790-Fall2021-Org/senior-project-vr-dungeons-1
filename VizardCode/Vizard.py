import viz
import vizact
import vizshape
import vizconnect
from vizconnect.util import view_collision
import csv

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
	
#example:
if IsThisVillanovaCAVE():
	#  =====================================
	#Position the view of the camera
	#CAVE specific:
	CONFIG_FILE = "E:\\VizardProjects\\_CaveConfigFiles\\vizconnect_config_CaveFloor+ART_headnode.py"
	vizconnect.go(CONFIG_FILE)
	viewPoint = vizconnect.addViewpoint(pos=[1,1,-7])
	viewPoint.add(vizconnect.getDisplay())
	vizconnect.resetViewpoints()
###############################################################
#p1 and p2 are points, each is an array of [x,y,z]
else:
	viz.go()	
	#boilerplate for my local laptop


#vizshape.addGrid(color=[0.2]*3).setPosition([0.5,1,0.5])

#Changes how lighting works around the main view
myLight = viz.MainView.getHeadLight()
myLight.disable()
myLight.color(viz.ORANGE)
myLight.intensity(0.5)
vizact.onkeydown('f', myLight.enable)
vizact.onkeydown('g', myLight.disable)

tex1 = viz.addTexture("stonewall.png")

scale = 1

#creates the master floor tile from which every other tile will be cloned and sets it at position [1,-1,0], underneath the floor
floor = vizshape.addQuad(size=(scale*1.0,scale*1.0),axis=vizshape.AXIS_Y,texture=tex1,lighting=True)
floor.setPosition([1,-1,0])

#reads from the csv file in GeneratorCode rechange to open('../GeneatorCode/output.csv)
with open('../GeneratorCode/output.csv') as csv_file:
	reader = csv.reader(csv_file, delimiter=',')
	data = list(reader)[0]

layout = []

#takes the first two numbers from the csv, which contain the width and height of the 2d dungeon
width = int(data.pop(0))
height = int(data.pop(0))

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

wall = vizshape.addBox(size=(scale*1.0,scale*1.0,scale*1.0),texture=tex1)
wall.color(viz.WHITE)

#iterate over every entry in the 2d list
for r in layout:
	row+=1
	col=-1
	for entry in r:
		col+=1
		#if there should be a floor at (row,col), clone the master floor to (row,1,col)
		if(entry=="false"):
			floor.copy().setPosition(scale*row,1,scale*col)
			
			#if(row == 0 or col == 0): #finds the empty space in the first row, the entrance
				#viz.MainView.setPosition([row+3.5,col+2.8, 0])
				#print(row)
				#print(col)
		else:
			wall.copy().setPosition(scale*row,1.5,scale*col)
			wall.copy().setPosition(scale*row,1.5+scale*1.0,scale*col)
			wall.copy().setPosition(scale*row,1.5+scale*2.0,scale*col)
		
view.setPosition([firstX*scale,3, firstY*scale])

#if not IsThisVillanovaCAVE():
#	viz.MainView.setPosition([startColumn+3.5,2.8,2.8])
#	print("made it here")
#	#sets the start position to 10 feet behind the entrance	
	
viz.MainView.collision(viz.ON)
		
	
print("Done")
print("firstX = ", firstX)
print("firstY = ", firstY)
print("getposition = ",view.getPosition())