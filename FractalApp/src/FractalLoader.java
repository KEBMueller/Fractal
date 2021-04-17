import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JFileChooser;

public class FractalLoader {

	public static void safeFractal(FractalMatrix fm) {
		File file = fm.file;
		
		JFileChooser chooser = new JFileChooser();
		int returnVal = 0;
		if(file == null) {
			returnVal = chooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				file = chooser.getSelectedFile();
			}
		}
		fm.name = file.getName();
			
		
			
		Integer[][] actualmatrix = fm.actualmatrix;
			
		try {
			file.createNewFile();
			PrintWriter out = new PrintWriter(file.getAbsolutePath());
			for(int i = 0; i < actualmatrix[0].length; i++) {
				for(int j = 0; j < actualmatrix.length; j++) {
					out.print(actualmatrix[j][i] + ",");
				}
				out.println();
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static FractalMatrix loadFractal() {
		JFileChooser chooser = new JFileChooser();
		int returnVal = 0;
		File file;
		returnVal = chooser.showOpenDialog(null);
		file = chooser.getSelectedFile();
		if(file != null) {
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
					String[] values = currentLine.split( "," );
					for(int j = 0 ; j<7 && j<values.length ; j++) {
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
