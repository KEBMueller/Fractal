import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JFileChooser;

public class FractalLoader {

	public static void safeFractal(FractalMatrix fm) {
		
	}
	
	public static FractalMatrix loadFractal() {
		JFileChooser chooser = new JFileChooser();
		int returnVal = 0;
		returnVal = chooser.showOpenDialog(null);
			
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			List<String> contend = null;
			try {
				contend = Files.readAllLines(file.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			FractalMatrix loadedMatrix = new FractalMatrix(FractalMatrix.MAX_LENGTH, file.getName());
			
			
			for(int i = 0; i < contend.size(); i++) {
				String currentLine = "";
				if(!contend.isEmpty()) {
					currentLine = contend.remove(0);
					String[] values = currentLine.split( ","  ,   7);
					for(int j = 0 ; j<7 ; j++) {
						int value = Integer.parseInt(values[j]);
						loadedMatrix.editmatrix(j, i, value);
					}
				}
			}
			
			loadedMatrix.file = file;
			
			return loadedMatrix;
			
		}
		return null;
	} 
}
