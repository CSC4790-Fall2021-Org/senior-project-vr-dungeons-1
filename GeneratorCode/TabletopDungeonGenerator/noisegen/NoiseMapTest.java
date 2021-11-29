package noisegen;

class NoiseMapTest {

    public static void main(String[] args) {
        NoiseMap nm = new NoiseMap(100,100,123,4);
        NoiseViewer nv = new NoiseViewer(nm,5);
        nv.setVisible(true);
    }

}
