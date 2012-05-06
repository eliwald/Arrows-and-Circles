package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import backend.Diagram;

import com.google.gson.stream.*;

/**
 * Represents a single project (single tab) of the FSM/Graph Diagram.
 * It consists of History Stack as well as utilities to save, open,
 * and export the diagram.
 * 
 * @author ajanthon
 */
public class DiagramProject {
	
	/** The number of maximum history. */
	private static final int MAX_HISTORY = 42; 
	
	/** The filename of this project. This could be null if it is a newly
	 * created diagram that has never been saved yet. */
	private String _filename; 
	
	/** The object that keeps track of recent changes to the diagram.
	 * It is useful for undo and redo features. */
	private HistoryStack _history;

	/** Boolean indicating whether the project is from a file, not a newly created one. */
	private boolean _loaded;
	
	/** Last saved revision number */
	private int _savedRevision; 
	
	/** Factory method that creates a new project. */
	public static DiagramProject newProject() {
		DiagramProject project = new DiagramProject();
		project._filename = "";
		project._history = new HistoryStack(MAX_HISTORY);
		project._loaded = false;
		project._savedRevision = -1;
		return project;
	}
	
	/** Factory method that loads the saved project from a file. 
	 * @throws FileNotFoundException */
	public static DiagramProject[] openProject() {
		
		// Pop up the file chooser for user to choose files.
		File[] files = openFileChooser();
		if(files == null) {
			return null; // No files selected.
		}
		
		// Trying to create diagram objects.
		ArrayList<DiagramProject> projects = new ArrayList<DiagramProject>();
		for(int i = 0; i < files.length; i++) {
			File file = files[i];
			Reader reader;
			try {
				reader = new FileReader(file);
			} catch (FileNotFoundException e) {
				continue;
			}
			Diagram loadedDiagram = getDiagramFromReader(reader);
			if(loadedDiagram == null) {
				// TODO: The file we tried to load is actually not a diagram file.
				continue;
			}
			
			DiagramProject project = new DiagramProject();
			project._filename = file.getName();
			project._history = new HistoryStack(MAX_HISTORY, loadedDiagram);
			project._loaded = true;
			project._savedRevision = 0;
			
			projects.add(project);
		}
		
		return (DiagramProject[]) projects.toArray();
	}

	private static Diagram getDiagramFromReader(Reader reader) {
		JsonReader jsonReader = new JsonReader(reader);
		try {
			while(jsonReader.hasNext()) {
				
			}
		} catch (IOException e) {
			return null;
		}
		return null;
	}
	
	private static File[] openFileChooser() {
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Diagram JSON File", "json");
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFiles();
		} else {
			return null;
		}
	}

	private static File saveFileChooser() {
		JFileChooser chooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Diagram JSON File", "json");
		chooser.addChoosableFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(true);
		
		int returnVal = chooser.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}

	
	
}
