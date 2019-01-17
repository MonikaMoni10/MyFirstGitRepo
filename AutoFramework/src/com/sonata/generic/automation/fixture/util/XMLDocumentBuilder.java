/*
 * Copyright 2015 Sonata Software. All rights reserved. Software, Inc.  All rights reserved.
 */

package com.sonata.generic.automation.fixture.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A wrapper class around a {@link DocumentBuilder} that remembers to reset
 * itself before doing any of its operations. This wrapper only exposes the
 * methods of the document builder that the tool is going to use.
 */
public class XMLDocumentBuilder
{
   private DocumentBuilder builder;
   private Document doc;
   
   private static String PATH = new File("").getAbsolutePath();

   /**
    * Constructs a new document builder that remembers to reset itself before
    * doing any of its operations.
    * 
    * @throws IllegalStateException
    *            The document builder could not be created with to satisfy the
    *            default configuration.
    */
   public XMLDocumentBuilder()
   {
      try
      {
         builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      }
      catch (ParserConfigurationException e)
      {
         throw new IllegalStateException("Failed to create document builder with default configuration", e);
      }
   }
   
   /**
    * Constructs a new document builder that will return a parsed document
    * 
    * @param file
    *           The xml file to parse. It's a relative path based on project root path.      
    */
   public XMLDocumentBuilder(final String file)
   {   
      String fullFilePath = PATH + "//" + file; 

      try {    
         builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         File fXmlFile = new File(fullFilePath);
         doc = builder.parse(fXmlFile);
         doc.getDocumentElement().normalize();
      } catch (ParserConfigurationException e1) {
         e1.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {      
         e.printStackTrace();
      }   
   }
   
   /**
    * get the text content of an element according to elementName and TestDataIdx
    * 
    * @param elementName 
    *           The element Name according to which the text is to be retrieved.
    * @param TestDataIdx 
    *           The index of the parent element(TestData) where the element is located.
    * @return 
    *            Return the element context according to elementName and parent TestDataIdx.       
    */
   
   public String getData(final String elementName, final int TestDataIdx)
   {
      NodeList nList = doc.getDocumentElement().getElementsByTagName("TestData");
      
      for (int i=0; i<nList.getLength(); i++) 
      {
         Node nNode = nList.item(i);
         if (nNode.getNodeType() == Node.ELEMENT_NODE) 
         {     
            Element eParent = (Element) nNode;
            
            if (eParent.getNodeName().equals("TestData") && eParent.hasAttribute("index")) 
            {
               if (eParent.getAttribute("index").equals(String.valueOf(TestDataIdx))) 
                  
                  return (getXMLElementText(eParent, elementName));
            }
         }    
      }
      
      return "";   
   }
   
   /**
    * get the text content of an element according to elementName and TestDataIdx
    * 
    * @param eParent 
    *           the parent element(TestData) where the element is located.
    * @param elementName 
    *           The element Name of a node.
    * @return 
    *           Return the text of an element.       
    */
   private String getXMLElementText(final Element eParent, final String elementName) 
   {    
      NodeList nList = eParent.getElementsByTagName(elementName);
      
      // if nList.getlength=0, means no element is found,
      // if nList.getlength>1, means duplicated elements existing, xml is corrupted, need refined
      if(nList.getLength() != 1)
         return "";
      
      Node nNode = nList.item(0);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) 
      {     
         Element element = (Element) nNode;
         if (element.getNodeName().equals(elementName))              
            return (element.getTextContent());
      }  
      return "";
   }
   
   /**
    * set the text content of an existing element or add the element if not existing 
    * according to elementName and TestDataIdx
    * 
    * @param elementName 
    *           The element Name according to which the text is to be updated or added.
    * @param TestDataIdx 
    *           The index of the parent element(TestData) where the element is located.
    * @param value 
    *           The element value to be updated or added.     
    * @return 
    *           Return true if the setData is successfully done, else return false. 
    */
   
   public boolean setData(final String elementName, final int TestDataIdx, final String value)
   {
      NodeList nList = doc.getDocumentElement().getElementsByTagName("TestData");
      for (int i=0; i<nList.getLength(); i++) 
      {
         Node nNode = nList.item(i);
         if (nNode.getNodeType() == Node.ELEMENT_NODE) 
         {     
            Element eParent = (Element) nNode;
            
            if (eParent.getNodeName().equals("TestData") && eParent.hasAttribute("index")) 
            {
               if (eParent.getAttribute("index").equals(String.valueOf(TestDataIdx))) 
                  return setXMLElementText(eParent, elementName, value);
            }
         }           
      }
      return false;
   }
   
   /**
    * set the text content of an existing element or add the element if not existing 
    * according to elementName and TestDataIdx
    * 
   * @param eParent 
    *           the parent element(TestData) where the element is to be updated or added.
    * @param elementName 
    *           The Name of an element.
    * @param value 
    *           The text value of an element. 
    * @return 
    *           Return true if the setData is successfully done, else return false.     
    */
   private boolean setXMLElementText(final Element eParent, final String elementName, final String value) 
   {    
      //NodeList nList = eParent.getChildNodes();
      NodeList nList = eParent.getElementsByTagName(elementName);
      // If the element is not existing, just add it.
      if(nList.getLength() ==0)
      {
         Element newElement = doc.createElement(elementName);
         newElement.setTextContent(value);
         eParent.appendChild(newElement);
      }
      // If the element is existing, just update it.
      else if(nList.getLength() ==1)
      {
         Node nNode = nList.item(0);
         if (nNode.getNodeName().trim().equals(elementName))
         {
            nNode.setTextContent(value);
         }
      }
      // If the element exists many times, means the xml file is corrupted, need check it.
      else
         return false;
      return true;
   }
   
   /**
    * Update the xml file.
    * @param file 
    *            The data configuration .xml file where the value to be updated.
    *            It's a relative path based on project root path.         
    * @throws IllegalStateException
    *            The document builder could not be created with to satisfy the
    *            default configuration.
    */
   public void updateXml(final String file)
   {
      String fullFilePath = PATH + "//" + file;
      
      try
      {
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(new File(fullFilePath));
         transformer.transform(source, result);

      }catch (TransformerException tfe)
      {
         tfe.printStackTrace();
      }
   }
   
   /**
    * Parses the content of the specified file into a DOM document.
    * 
    * @param file
    *           The file to parse.
    * 
    * @return The DOM document representing the parsed file.
    * @throws SAXException
    *            A parse error occurred.
    * @throws IOException
    *            A file IO error occurred.
    * @throws IllegalArgumentException
    *            The file was null.
    */
   public Document parse(final File file) throws SAXException, IOException
   {
      builder.reset();
      return builder.parse(file); // may throw IllegalArgumentException too
   }
}
