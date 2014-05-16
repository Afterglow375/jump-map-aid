package VMFObject;

public class Vertex {
	
	public float x;
	public float y;
	public float z;
	
	public Vertex(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vertex(String coords){ //in "(x, y, z)" format
		String[] xyz = coords.substring(1, coords.length()-1).split(", ");
		this.x = Integer.parseInt(xyz[0]);
		this.y = Integer.parseInt(xyz[1]);
		this.z = Integer.parseInt(xyz[2]);
	}
	
	public double distanceTo(Vertex v){
		return Math.sqrt(Math.pow(this.x-v.x, 2)+Math.pow(this.y-v.y, 2)+Math.pow(this.z-v.z, 2));
	}
	
	public String toString(){
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}
