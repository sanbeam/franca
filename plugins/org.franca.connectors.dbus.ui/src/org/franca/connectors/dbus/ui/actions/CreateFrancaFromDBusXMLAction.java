/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.franca.connectors.dbus.ui.actions;

import java.util.Collection;

import model.emf.dbusxml.NodeType;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.xtext.validation.Issue;
import org.franca.connectors.dbus.DBusConnector;
import org.franca.connectors.dbus.DBusModelContainer;
import org.franca.core.dsl.FrancaPersistenceManager;
import org.franca.core.dsl.ui.util.SpecificConsole;
import org.franca.core.franca.FModel;
import org.franca.core.utils.FrancaRecursiveValidator;

import com.google.inject.Inject;


public class CreateFrancaFromDBusXMLAction implements IObjectActionDelegate {

	private IStructuredSelection selection = null;
	
//	@Inject FrancaPersistenceManager loader;
//	@Inject FrancaRecursiveValidator validator;

	@Override
	public void run(IAction action) {
    	SpecificConsole myConsole = new SpecificConsole("Franca");
        final MessageConsoleStream out = myConsole.getOut();
        final MessageConsoleStream err = myConsole.getErr();
		
        if (selection!=null) {
            if (selection.size()!=1) {
            	err.println("Please select exactly one file with extension 'xml'!");
                return;
            }

            IFile file = (IFile)selection.getFirstElement();
//          String dbusFile = file.getLocationURI().toString();
            String dbusFile = file.getFullPath().toString();
            String outputDir = file.getParent().getLocation().toString();

    		// load D-Bus Introspection XML file
            out.println("Loading D-Bus Introspection XML file '" + dbusFile + "' ...");
    		DBusConnector conn = new DBusConnector();
    		DBusModelContainer dbus = (DBusModelContainer)conn.loadModel(dbusFile);
    		if (dbus==null) {
    			err.println("Couldn't load D-Bus Introspection XML file '" + dbusFile + "'.");
    			return;
    		}
    		NodeType node = dbus.model();
    		out.println("D-Bus Introspection XML: loaded node '" + node.getName() + "'");
    		
//    		// validate resource
//    		Collection<Issue> issues = validator.validate(fmodel.eResource());
//    		int nErrors = 0;
//    		for(Issue issue : issues) {
//    			switch (issue.getSeverity()) {
//    			case INFO:
//    			case WARNING:
//    				out.println(issue.toString());
//    				break;
//    			case ERROR:
//    				err.println(issue.toString());
//    				nErrors++;
//    				break;
//    			}
//    		}
//    		if (nErrors>0) {
//    			err.println("Aborting due to validation errors!");
//    			return;
//    		}
//  
//    		// transform DBus Introspection XML to Franca
//    		out.println("Transforming from DBus Introspection XML file ...");
//    		DBusConnector dbusConn = new DBusConnector();
//    		DBusModelContainer dbus = null;
////    		try {
//    			dbus = (DBusModelContainer) dbusConn.fromFranca(fmodel);
////    		} catch (Exception e) {
////    			err.println("Internal transformation error, aborting.");
////				return;
////    		}
//    		
//    		// save Franca fidl file
//    		int ext = file.getName().lastIndexOf("." + file.getFileExtension());
//    		String outfile = file.getName().substring(0, ext) + ".xml";
//    		String outpath = outputDir + "/" + outfile;
//    		if (dbusConn.saveModel(dbus, outpath)) {
//    			out.println("Saved DBus Introspection file '" + outpath + "'.");
//    		} else {
//    			err.println("DBus Introspection file couldn't be written to file '" + outpath + "'.");
//    		}
//            
//    		// refresh IDE (in order to make new files visible)
//            IProject project = file.getProject();
//            try {
//    			project.refreshLocal(IResource.DEPTH_INFINITE, null);;
//			} catch (CoreException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
        this.selection = (IStructuredSelection)selection;
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
	}


}