package afterglow.mapping;

import java.util.ArrayList;

// This class transforms existing brushes into other aspects of VMF files - solids, groups, entities
public class VMFObjectCreator {
	private static int playerClipCounter = 0;
	private static int funcBrushCounter = 0;
	private static int triggerTeleportCounter = 0;
	private static int noGrenadesCounter = 0;
	private ArrayList<String> brush;
	private ArrayList<Integer> indices;
	public static ArrayList<String> outputBrushesAndGroups = new ArrayList<String>();
	public static ArrayList<String> outputEntities = new ArrayList<String>();
	
	public VMFObjectCreator(ArrayList<String> brush, ArrayList<Integer> indices) {
		this.brush = brush;
		this.indices = indices;
	}

	// The origin of a brush entity can be calculated by finding all the unique points that make up the solid,
	// comparing each point and find the pair that results in the largest distance between them. 
	// Halfway between this pair of points is the origin.
	private String computeOrigin() {
		ArrayList<String> points = new ArrayList<String>();
		int firstParen, secondParen, thirdParen;
		String line, firstPoint, secondPoint, thirdPoint;
		
		for (int i = 0; i < indices.size(); i++) { // Extract the unique points
			line = brush.get(indices.get(i) + 2);
			firstParen = line.indexOf("(");
			firstPoint = line.substring(firstParen + 1, line.indexOf(")"));
			secondParen = line.indexOf("(", firstParen + 1);
			secondPoint = line.substring(secondParen + 1, line.indexOf(")", secondParen));
			thirdParen = line.indexOf("(", secondParen + 1);
			thirdPoint = line.substring(thirdParen + 1, line.indexOf(")", thirdParen));
			
			if (!points.contains(firstPoint)) {
				points.add(firstPoint);
			}
			if (!points.contains(secondPoint)) {
				points.add(secondPoint);
			}
			if (!points.contains(thirdPoint)) {
				points.add(thirdPoint);
			}
		}
		
		double maxDistSoFar = 0;
		double dist;
		int maxPointSoFar1 = 0;
		int maxPointSoFar2 = 0;
		String[] splitPoint1, splitPoint2;
		double coordinateDiffX, coordinateDiffY, coordinateDiffZ;
		for (int i = 0; i < points.size(); i++) { // Comparing the unique points
			splitPoint1 = points.get(i).split(" ");
			for (int j = i + 1; j < points.size(); j++) {
				splitPoint2 = points.get(j).split(" ");
				coordinateDiffX = Double.parseDouble(splitPoint1[0]) - Double.parseDouble(splitPoint2[0]);
				coordinateDiffY = Double.parseDouble(splitPoint1[1]) - Double.parseDouble(splitPoint2[1]);
				coordinateDiffZ = Double.parseDouble(splitPoint1[2]) - Double.parseDouble(splitPoint2[2]);
				dist = Math.sqrt((coordinateDiffX*coordinateDiffX + coordinateDiffY*coordinateDiffY + coordinateDiffZ*coordinateDiffZ));
				if (dist > maxDistSoFar) {
					maxDistSoFar = dist;
					maxPointSoFar1 = i;
					maxPointSoFar2 = j;
				}
			}
		}
		
		// Midpoint function
		splitPoint1 = points.get(maxPointSoFar1).split(" ");
		splitPoint2 = points.get(maxPointSoFar2).split(" ");
		int originX = (int)(Double.parseDouble(splitPoint1[0]) + Double.parseDouble(splitPoint2[0]))/2;
		int originY = (int)(Double.parseDouble(splitPoint1[1]) + Double.parseDouble(splitPoint2[1]))/2;
		int originZ =(int)(Double.parseDouble(splitPoint1[2]) + Double.parseDouble(splitPoint2[2]))/2;
		String origin = Integer.toString(originX) + " " + Integer.toString(originY) + " " + Integer.toString(originZ);
		return origin;
	}
	
	private int createGroup() {
		int id = ParseVMF.generateUniqueId();
		String group =
		"\tgroup\n"
		+ "\t{\n"
		+ "\t\t\"id\" \"" + id + "\"\n"
		+ "\t\teditor\n"
		+ "\t\t{\n"
		+ "\t\t\t\"color\" \"208 157 0\"\n"
		+ "\t\t\t\"visgroupshown\" \"1\"\n"
		+ "\t\t\t\"visgroupautoshown\" \"1\"\n"
		+ "\t\t}\n"
		+ "\t}";
		outputBrushesAndGroups.add(group);
		return id;
	}
	
	public void createTeleport() {
		String teleport = 
		"entity\n"
		+ "{\n"
		+ "\t\"id\" \"" + ParseVMF.generateUniqueId() + "\"\n"
		+ "\t\"classname\" \"trigger_teleport\"\n"
		+ "\t\"origin\" \"" + computeOrigin() + "\"\n"
		+ "\t\"spawnflags\" \"1\"\n"
		+ "\t\"StartDisabled\" \"0\"\n";
	}
	
	public void createPlayerClip() {
		int groupId = createGroup();
		String illusionary = 
		"entity\n"
		+ "{\n"
		+ "\t\"id\" \"" + ParseVMF.generateUniqueId() + "\"\n"
		+ "\t\"classname\" \"func_illusionary\"\n"
		+ "\t\"disablereceiveshadows\" \"0\"\n"
		+ "\t\"disableshadows\" \"0\"\n"
		+ "\t\"origin\" \"" + computeOrigin() + "\"\n"
		+ "\t\"renderamt\" \"255\"\n"
		+ "\t\"rendercolor\" \"255 255 255\"\n"
		+ "\t\"renderfx\" \"0\"\n"
		+ "\t\"rendermode\" \"0\"\n";
		String playerClip = "";
		for (String line : brush) {
			if (line.contains("\"material\"")) {
				illusionary += line + "\n";
				playerClip += "\t\t\t\"material\" \"TOOLS/TOOLSPLAYERCLIP\"\n"; // Replace textures with player clip
			}
			else if (line.contains("\t\t\"id\"")) {
				if (line.contains("\t\t\t")) {
					illusionary += "\t\t\t\"id\" \"" + ParseVMF.generateUniqueId() + "\"\n";
					playerClip += "\t\t\t\"id\" \"" + ParseVMF.generateUniqueId() + "\"\n";
				}
				else {
					illusionary += "\t\t\"id\" \"" + ParseVMF.generateUniqueId() + "\"\n";
					playerClip += "\t\t\"id\" \"" + ParseVMF.generateUniqueId() + "\"\n";
				}
			}
			else if (line.contains("\"visgroupshown\"")) {
				playerClip += "\t\t\t\"groupid\" \"" + groupId + "\"\n";
				playerClip += line + "\n";
			}
			else {
				playerClip += line + "\n";
				illusionary += line + "\n";
			}
		}
		illusionary +=
		"\teditor\n"
		+ "\t{\n"
		+ "\t\t\"color\" \"220 30 220\"\n"
		+ "\t\t\"groupid\" \"" + groupId + "\"\n"
		+ "\t\t\"visgroupshown\" \"1\"\n"
		+ "\t\t\"visgroupautoshown\" \"1\"\n"
		+ "\t\t\"comments\" \"Auto-generated by Jump Map Aid .90\"\n"
		+ "\t\t\"logicalpos\" \"[0, 1000]\"\n"
		+ "\t}\n"
		+ "}";
		playerClipCounter++;
		outputEntities.add(illusionary);
		outputBrushesAndGroups.add(0, playerClip.substring(0, playerClip.length()-1));
	}
	
	public void createFuncBrush() {
		String funcBrush = 
		"entity\n"
		+ "{\n"
		+ "\t\"id\" \"" + ParseVMF.generateUniqueId() + "\"\n"
		+ "\t\"classname\" \"func_brush\"\n"
		+ "\t\"disablereceiveshadows\" \"0\"\n"
		+ "\t\"disableshadows\" \"0\"\n"
		+ "\t\"InputFilter\" \"0\"\n"
		+ "\t\"invert_exclusion\" \"0\"\n"
		+ "\t\"origin\" \"" + computeOrigin() + "\"\n"
		+ "\t\"renderamt\" \"255\"\n"
		+ "\t\"rendercolor\" \"255 255 255\"\n"
		+ "\t\"renderfx\" \"0\"\n"
		+ "\t\"rendermode\" \"0\"\n"
		+ "\t\"solidbsp\" \"0\"\n"
		+ "\t\"solidity\" \"0\"\n"
		+ "\t\"spawnflags\" \"2\"\n"
		+ "\t\"StartDisabled\" \"0\"\n"
		+ "\t\"vrad_brush_cast_shadows\" \"0\"\n";
		for (String line : brush) {
			funcBrush += line + "\n";
		}
		funcBrush +=
		"\teditor\n"
		+ "\t{\n"
		+ "\t\t\"color\" \"220 30 220\"\n"
		+ "\t\t\"visgroupshown\" \"1\"\n"
		+ "\t\t\"visgroupautoshown\" \"1\"\n"
		+ "\t\t\"comments\" \"Auto-generated by Jump Map Aid .90\"\n"
		+ "\t\t\"logicalpos\" \"[0, 1000]\"\n"
		+ "\t}\n"
		+ "}";
		
		funcBrushCounter++;
		outputEntities.add(funcBrush);
	}
	
	// Getters
	public static int getPlayerClipCounter() {
		return playerClipCounter;
	}
	
	public static int getFuncBrushCounter() {
		return funcBrushCounter;
	}
	
	public static int getTriggerTeleportCounter() {
		return triggerTeleportCounter;
	}
	
	public static int getNoGrenadesCounter() {
		return noGrenadesCounter;
	}
}
