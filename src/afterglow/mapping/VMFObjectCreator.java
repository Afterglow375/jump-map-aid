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

	// The origin of a brush entity can be calculated by finding all the unique points that make up the solid,
	// comparing each point and find the pair that results in the largest distance between them. 
	// Halfway between this pair of points is the origin.
	public String computeOrigin() {
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
		Internalize.log.append(origin + '\n');
		return origin;
	}
	
	public String createFuncBrush() {
		String origin = computeOrigin();
//		ArrayList<String> points = new ArrayList<String>();
//		for (int i = 0; i < indices.size(); i++) {
//			
//		}
		return origin;
	}
}
