package afterglow.mapping;

import java.util.ArrayList;

// This class transforms existing brushes into other aspects of VMF files - solids, groups, entities
public class VMFObjectCreator {
	private ArrayList<String> brush;
	private ArrayList<Integer> indices;
	
	public VMFObjectCreator(ArrayList<String> brush, ArrayList<Integer> indices) {
		this.brush = brush;
		this.indices = indices;
	}

	public String generateOrigin() {
		ArrayList<String> points = new ArrayList<String>();
		for (int i = 0; i < indices.size(); i++) {
			
		}
	}
	
	public String createFuncBrush() {
		String origin = generateOrigin();
		ArrayList<String> points = new ArrayList<String>();
		for (int i = 0; i < indices.size(); i++) {
			
		}
	}
}
