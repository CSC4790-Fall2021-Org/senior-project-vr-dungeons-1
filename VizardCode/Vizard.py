import viz
import vizact
import vizshape
import csv

viz.fov(90)
viz.go()

#vizshape.addGrid(color=[0.2]*3).setPosition([0.5,1,0.5])

#creates the master floor tile from which every other tile will be cloned and sets it at position [1,-1,0], underneath the floor
floor = vizshape.addQuad(size=(1.0,1.0),axis=vizshape.AXIS_Y,texture=viz.add("white.jpg"),lighting=False)
floor.setPosition([1,-1,0])

#reads from the csv file in GeneratorCode rechange to open('../GeneatorCode/output.csv)
with open('../GeneratorCode/output.csv') as csv_file:
	reader = csv.reader(csv_file, delimiter=',')
	data = list(reader)[0]

layout = []

#takes the first two numbers from the csv, which contain the width and height of the 2d dungeon
width = int(data.pop(0))
height = int(data.pop(0))

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
box = vizshape.addBox(size=(1.0,3.0,1.0),texture=viz.addTexture("north.png"))
box.color(viz.WHITE)
#iterate over every entry in the 2d list
for r in layout:
	row+=1
	col=-1
	for entry in r:
		col+=1
		#if there should be a floor at (row,col), clone the master floor to (row,1,col)
		if(entry=="false"):
			floor.copy().setPosition(row,1,col)
		else:
			box.copy().setPosition(row,1,col)
			
			
print("Done")
