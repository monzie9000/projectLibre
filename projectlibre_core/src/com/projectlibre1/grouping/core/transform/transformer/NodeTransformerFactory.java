/*******************************************************************************
 * The contents of this file are subject to the Common Public Attribution License 
 * Version 1.0 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.projectlibre.com/license . The License is based on the Mozilla Public 
 * License Version 1.1 but Sections 14 and 15 have been added to cover use of 
 * software over a computer network and provide for limited attribution for the 
 * Original Developer. In addition, Exhibit A has been modified to be consistent 
 * with Exhibit B. 
 *
 * Software distributed under the License is distributed on an "AS IS" basis, 
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the 
 * specific language governing rights and limitations under the License. The 
 * Original Code is ProjectLibre. The Original Developer is the Initial Developer 
 * and is ProjectLibre Inc. All portions of the code written by ProjectLibre are 
 * Copyright (c) 2012-2019. All Rights Reserved. All portions of the code written by 
 * ProjectLibre are Copyright (c) 2012-2019. All Rights Reserved. Contributor 
 * ProjectLibre, Inc.
 *
 * Alternatively, the contents of this file may be used under the terms of the 
 * ProjectLibre End-User License Agreement (the ProjectLibre License) in which case 
 * the provisions of the ProjectLibre License are applicable instead of those above. 
 * If you wish to allow use of your version of this file only under the terms of the 
 * ProjectLibre License and not to allow others to use your version of this file 
 * under the CPAL, indicate your decision by deleting the provisions above and 
 * replace them with the notice and other provisions required by the ProjectLibre 
 * License. If you do not delete the provisions above, a recipient may use your 
 * version of this file under either the CPAL or the ProjectLibre Licenses. 
 *
 *
 * [NOTE: The text of this Exhibit A may differ slightly from the text of the notices 
 * in the Source Code files of the Original Code. You should use the text of this 
 * Exhibit A rather than the text found in the Original Code Source Code for Your 
 * Modifications.] 
 *
 * EXHIBIT B. Attribution Information for ProjectLibre required
 *
 * Attribution Copyright Notice: Copyright (c) 2012-2019, ProjectLibre, Inc.
 * Attribution Phrase (not exceeding 10 words): 
 * ProjectLibre, open source project management software.
 * Attribution URL: http://www.projectlibre.com
 * Graphic Image as provided in the Covered Code as file: projectlibre-logo.png with 
 * alternatives listed on http://www.projectlibre.com/logo 
 *
 * Display of Attribution Information is required in Larger Works which are defined 
 * in the CPAL as a work which combines Covered Code or portions thereof with code 
 * not governed by the terms of the CPAL. However, in addition to the other notice 
 * obligations, all copies of the Covered Code in Executable and Source Code form 
 * distributed must, as a form of attribution of the original author, include on 
 * each user interface screen the "ProjectLibre" logo visible to all users. 
 * The ProjectLibre logo should be located horizontally aligned with the menu bar 
 * and left justified on the top left of the screen adjacent to the File menu. The 
 * logo must be at least 144 x 31 pixels. When users click on the "ProjectLibre" 
 * logo it must direct them back to http://www.projectlibre.com. 
 *******************************************************************************/
package com.projectlibre1.grouping.core.transform.transformer;

import groovy.lang.GroovyClassLoader;

import com.projectlibre1.field.InvalidFormulaException;
import com.projectlibre1.grouping.core.transform.CommonTransform;
import com.projectlibre1.grouping.core.transform.CommonTransformFactory;

/**
 *
 */
public class NodeTransformerFactory extends CommonTransformFactory{
//	static Log log = LogFactory.getLog(NodeTransformerFactory.class);
	protected String type = null; 
	
	private static int count=0;
	
	
	
	public CommonTransform getTransform() throws InvalidFormulaException{
	    CommonTransform t=getTransformFromDefinition();
	    if (t!=null){
			setProperties(t);
	        return t;
	    }
	    if (formulaText==null) return null;
	    StringBuffer classText=new StringBuffer();
	    classText.append("package com.projectlibre1.grouping.core.transform.transformer;\n");
	    classText.append("import com.projectlibre1.grouping.core.Node;\n");
	    classText.append("import com.projectlibre1.datatype.*;\n");
	    classText.append("import com.projectlibre1.pm.assignment.Assignment;\n");
	    classText.append("public class TransformerFormula").append(count++).append(" extends NodeTransformer{\n");
	    classText.append("\tpublic Object evaluate(Object ").append(type).append("){\n")
	    		.append("\t\t").append(formulaText).append("\n\t}\n");
	    classText.append("}\n");
//	    GroovyClassLoader loader = new GroovyClassLoader(ClassLoaderUtils.getLocalClassLoader());
	    GroovyClassLoader loader = new GroovyClassLoader(getClass().getClassLoader());
		try {
			Class groovyClass = loader.parseClass(classText.toString()); //TODO this his horribly slow (~500ms)  Can we parse all at once or can we do this lazily or initialize in another thread?
			t=(CommonTransform)groovyClass.newInstance();
			setProperties(t);
			return t;
		} catch (Exception e) {
//			e.printStackTrace();
//		    System.out.println("classText="+classText);
			throw new InvalidFormulaException(e);
		}
	}

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    
    
    
    

}
