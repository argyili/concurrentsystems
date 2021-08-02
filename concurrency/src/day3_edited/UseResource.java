package day3_edited;

public class UseResource extends Thread {
	private final Resource r;
	
	UseResource(Resource r, int id) {
		super(Integer.toString(id));
		this.r = r;
	}
	
	public void run() {
		while (true) {
			r.access();
		}
	}
}
