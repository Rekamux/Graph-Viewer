package main;

import controller.MainController;

public class Main 
{
	public static void main(String[] args) 
	{
		MainController controller = new MainController();
		controller.showProgram();
	}
	
	/*void createJar(Main mainType) 
	{
        Shell parentShell= null;
        JarPackageData description= new JarPackageData();
        IPath location= new Path("C:/tmp/myjar.jar");
        description.setJarLocation(location);
        description.setSaveManifest(true);
        description.setManifestMainClass(mainType);
        description.setElements(filestoExport);
        IJarExportRunnable runnable= description.createJarExportRunnable(parentShell);
        try {
            new ProgressMonitorDialog(parentShell).run(true,true, runnable);
        } catch (InvocationTargetException e) {
            // An error has occurred while executing the operation
        } catch (InterruptedException e) {
            // operation has been canceled.
        }
    }*/
}
