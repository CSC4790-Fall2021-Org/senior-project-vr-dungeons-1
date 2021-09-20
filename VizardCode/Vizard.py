import viz
import vizact
import vizshape

viz.fov(90)
viz.go()

vizshape.addGrid(color=[0.2]*3).setPosition([0.5,1,0.5])

floor = vizshape.addQuad(size=(1.0,1.0),axis=vizshape.AXIS_Y,texture=viz.add("white.jpg"),)
floor.setPosition([1,1,0])