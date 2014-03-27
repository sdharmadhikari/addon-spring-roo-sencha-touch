package com.softrism.roo.addon.senchatouch;

import static org.springframework.roo.model.JavaType.LONG_OBJECT;
import static org.springframework.roo.model.JdkJavaType.BIG_DECIMAL;
import static org.springframework.roo.model.Jsr303JavaType.FUTURE;
import static org.springframework.roo.model.Jsr303JavaType.MIN;
import static org.springframework.roo.model.Jsr303JavaType.PAST;
import static org.springframework.roo.model.SpringJavaType.DATE_TIME_FORMAT;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import com.softrism.roo.addon.senchatouch.velocity.VelocityEnabler;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.addon.web.mvc.controller.details.WebMetadataService;
import org.springframework.roo.addon.web.mvc.controller.scaffold.WebScaffoldMetadata;
import org.springframework.roo.addon.web.mvc.jsp.menu.MenuOperations;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.FieldMetadata;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.operations.DateTime;
import org.springframework.roo.classpath.persistence.PersistenceMemberLocator;
import org.springframework.roo.classpath.scanner.MemberDetailsScanner;
import org.springframework.roo.metadata.MetadataService;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.FeatureNames;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.util.FileUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.softrism.roo.addon.senchatouch.beans.AppBean;
import com.softrism.roo.addon.senchatouch.beans.EntityBean;


/**
 * Implementation of {@link SenchaTouchOperations}.
 * 
 * @author Stefan Schmidt
 * @since 1.0
 */
@Component
@Service
public class SenchaTouchOperationsImpl implements SenchaTouchOperations {

    private static final Logger LOGGER = HandlerUtils
            .getLogger(SenchaTouchOperationsImpl.class);

    @Reference private FileManager fileManager;
    @Reference private MemberDetailsScanner memberDetailsScanner;
    @Reference private MenuOperations menuOperations;
    @Reference private MetadataService metadataService;
    @Reference private PathResolver pathResolver;
    @Reference private PersistenceMemberLocator persistenceMemberLocator;
    @Reference private ProjectOperations projectOperations;
    @Reference private TypeLocationService typeLocationService;
    @Reference private WebMetadataService webMetadataService;

    private String TEMPLATE_ROOT = "templates/";
    /**
     * Creates Sencha Touch code.
     * 
     * @param controller the JavaType of the controller under test (required)
     * @param name the name of the test case (optional)
     */
    public void generateSenchaTouchCode(final JavaType controller, String name,
            String serverURL) {

        if (!serverURL.endsWith("/")) {
            serverURL = serverURL + "/";   // Here I may want to add app name to the end.
        }


        VelocityEnabler velocityEnabler = new VelocityEnabler();

        AppBean appBean = new AppBean("SenchaCrud");

        ArrayList<String> allEntities = getAllValidEntities(controller);

        InputStream is = SenchaTouchOperationsImpl.class.getClassLoader().getResourceAsStream("common-templates.list");

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String fileName;
        try {
            while((fileName = br.readLine()) != null){
                System.out.println(fileName);
                String templateFile = TEMPLATE_ROOT + fileName;
                //final InputStream templateInputStream = FileUtils.getInputStream(
                       // getClass(), templateFile);
                //Validate.notNull(templateInputStream,
                //        "Could not acquire " + templateFile+ " template");

                for(String entityName : allEntities) {
                    EntityBean entityBean = new EntityBean( entityName);
                    String parsedString = velocityEnabler.velocityExecute(templateFile, appBean, entityBean);
                    createEntityJsFile(entityName, fileName, parsedString);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("Ended addon-roo-sencha successfully!..");

        //controller.getSimpleTypeName();
        //
        //        serverURL
        //                + projectOperations.getProjectName(projectOperations
        //                        .getFocusedModuleName()) + "/"
        //                + webScaffoldMetadata.getAnnotationValues().getPath()
        //                + "?form"));
        //
        //final ClassOrInterfaceTypeDetails formBackingTypeDetails = typeLocationService
        //        .getTypeDetails(formBackingType);
        //Validate.notNull(
        //        formBackingType,
        //        "Class or interface type details for type '%s' could not be resolved",
        //        formBackingType);
        //final MemberDetails memberDetails = memberDetailsScanner
        //        .getMemberDetails(getClass().getName(), formBackingTypeDetails);

        // Add composite PK identifier fields if needed

        /*     Getting all fields
        for (final FieldMetadata field : persistenceMemberLocator
                .getEmbeddedIdentifierFields(formBackingType)) {
            final JavaType fieldType = field.getFieldType();
            if (!fieldType.isCommonCollectionType()
                    && !isSpecialType(fieldType)) {
                final FieldMetadataBuilder fieldBuilder = new FieldMetadataBuilder(
                        field);
                final String fieldName = field.getFieldName().getSymbolName();
                fieldBuilder.setFieldName(new JavaSymbolName(fieldName + "."
                        + fieldName));
                tbody.appendChild(typeCommand(document, fieldBuilder.build()));
            }
        }
        */
        /*
        // Add all other fields
        final List<FieldMetadata> fields = webMetadataService
                .getScaffoldEligibleFieldMetadata(formBackingType,
                        memberDetails, null);
        for (final FieldMetadata field : fields) {
            final JavaType fieldType = field.getFieldType();
            if (!fieldType.isCommonCollectionType()
                    && !isSpecialType(fieldType)) {
                tbody.appendChild(typeCommand(document, field));
            }
        }
        */
        //tbody.appendChild(clickAndWaitCommand(document,
        //        "//input[@id = 'proceed']"));
        /*
        // Add verifications for all other fields
        for (final FieldMetadata field : fields) {
            final JavaType fieldType = field.getFieldType();
            if (!fieldType.isCommonCollectionType()
                    && !isSpecialType(fieldType)) {
                tbody.appendChild(verifyTextCommand(document, formBackingType,
                        field));
            }
        }
        */
        /*
        fileManager.createOrUpdateTextFileIfRequired(senchaTouchPath,
                XmlUtils.nodeToString(document), false);
        */
        //manageTestSuite(relativeTestFilePath, name, serverURL);


    }

    public boolean createEntityJsFile(String entityName, String templateName, String fileContent ) {
        final String relativeFilePath = "" + templateName;

        System.out.println("relativeControllerTestFilePath : " + relativeFilePath);

        final String finalFilePath = pathResolver.getFocusedIdentifier(
                Path.SRC_MAIN_WEBAPP, relativeFilePath);
        System.out.println("relativeControllerTestFilePath : " + finalFilePath);

        fileManager.createOrUpdateTextFileIfRequired(finalFilePath,
               fileContent , false);

        return true;
    }

    public boolean isSenchaTouchInstallationPossible() {
        return projectOperations.isFocusedProjectAvailable()
                && projectOperations.isFeatureInstalled(FeatureNames.MVC);
    }

    public ArrayList<String> getAllValidEntities(final JavaType controller) { // Currently only one controller, later no arguments because all controllers.

        Validate.notNull(controller, "Controller type required");

        final ClassOrInterfaceTypeDetails controllerTypeDetails = typeLocationService
                .getTypeDetails(controller);
        Validate.notNull(
                controllerTypeDetails,
                "Class or interface type details for type '%s' could not be resolved",
                controller);


        final LogicalPath path = PhysicalTypeIdentifier
                .getPath(controllerTypeDetails.getDeclaredByMetadataId());
        final String webScaffoldMetadataIdentifier = WebScaffoldMetadata
                .createIdentifier(controller, path);
        final WebScaffoldMetadata webScaffoldMetadata = (WebScaffoldMetadata) metadataService
                .get(webScaffoldMetadataIdentifier);
        Validate.notNull(
                webScaffoldMetadata,
                "Web controller '%s' does not appear to be an automatic, scaffolded controller",
                controller.getFullyQualifiedTypeName());


        // We abort the creation of a senchatouch code if the controller does not
        // allow the creation of new instances for the form backing object
        if (!webScaffoldMetadata.getAnnotationValues().isCreate()) {
            LOGGER.warning("The controller you specified does not allow the creation of new instances of the form backing object. No Sencha Touch code created.");
            return null;
        }


        final JavaType formBackingType = webScaffoldMetadata
                .getAnnotationValues().getFormBackingObject();

        ArrayList<String> allEntityNames = new ArrayList<String>();
        String entityName = formBackingType.getSimpleTypeName();
        allEntityNames.add(entityName);
        return allEntityNames;
    }

    private boolean isSpecialType(final JavaType javaType) {
        return typeLocationService.isInProject(javaType);
    }



    private String convertToInitializer(final FieldMetadata field) {
        String initializer = " ";
        short index = 1;
        final AnnotationMetadata min = MemberFindingUtils.getAnnotationOfType(
                field.getAnnotations(), MIN);
        if (min != null) {
            final AnnotationAttributeValue<?> value = min
                    .getAttribute(new JavaSymbolName("value"));
            if (value != null) {
                index = new Short(value.getValue().toString());
            }
        }
        final JavaType fieldType = field.getFieldType();
        if (field.getFieldName().getSymbolName().contains("email")
                || field.getFieldName().getSymbolName().contains("Email")) {
            initializer = "some@email.com";
        }
        else if (fieldType.equals(JavaType.STRING)) {
            initializer = "some"
                    + field.getFieldName()
                    .getSymbolNameCapitalisedFirstLetter() + index;
        }
        else if (fieldType.equals(new JavaType(Date.class.getName()))
                || fieldType.equals(new JavaType(Calendar.class.getName()))) {
            final Calendar cal = Calendar.getInstance();
            AnnotationMetadata dateTimeFormat = null;
            String style = null;
            if ((dateTimeFormat = MemberFindingUtils.getAnnotationOfType(
                    field.getAnnotations(), DATE_TIME_FORMAT)) != null) {
                final AnnotationAttributeValue<?> value = dateTimeFormat
                        .getAttribute(new JavaSymbolName("style"));
                if (value != null) {
                    style = value.getValue().toString();
                }
            }
            if (MemberFindingUtils.getAnnotationOfType(field.getAnnotations(),
                    PAST) != null) {
                cal.add(Calendar.YEAR, -1);
                cal.add(Calendar.MONTH, -1);
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            else if (MemberFindingUtils.getAnnotationOfType(
                    field.getAnnotations(), FUTURE) != null) {
                cal.add(Calendar.YEAR, 1);
                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            if (style != null) {
                if (style.startsWith("-")) {
                    initializer = ((SimpleDateFormat) DateFormat
                            .getTimeInstance(
                                    DateTime.parseDateFormat(style.charAt(1)),
                                    Locale.getDefault())).format(cal.getTime());
                }
                else if (style.endsWith("-")) {
                    initializer = ((SimpleDateFormat) DateFormat
                            .getDateInstance(
                                    DateTime.parseDateFormat(style.charAt(0)),
                                    Locale.getDefault())).format(cal.getTime());
                }
                else {
                    initializer = ((SimpleDateFormat) DateFormat
                            .getDateTimeInstance(
                                    DateTime.parseDateFormat(style.charAt(0)),
                                    DateTime.parseDateFormat(style.charAt(1)),
                                    Locale.getDefault())).format(cal.getTime());
                }
            }
            else {
                initializer = ((SimpleDateFormat) DateFormat.getDateInstance(
                        DateFormat.SHORT, Locale.getDefault())).format(cal
                        .getTime());
            }

        }
        else if (fieldType.equals(JavaType.BOOLEAN_OBJECT)
                || fieldType.equals(JavaType.BOOLEAN_PRIMITIVE)) {
            initializer = Boolean.valueOf(false).toString();
        }
        else if (fieldType.equals(JavaType.INT_OBJECT)
                || fieldType.equals(JavaType.INT_PRIMITIVE)) {
            initializer = Integer.valueOf(index).toString();
        }
        else if (fieldType.equals(JavaType.DOUBLE_OBJECT)
                || fieldType.equals(JavaType.DOUBLE_PRIMITIVE)) {
            initializer = Double.toString(index);
        }
        else if (fieldType.equals(JavaType.FLOAT_OBJECT)
                || fieldType.equals(JavaType.FLOAT_PRIMITIVE)) {
            initializer = Float.toString(index);
        }
        else if (fieldType.equals(LONG_OBJECT)
                || fieldType.equals(JavaType.LONG_PRIMITIVE)) {
            initializer = Long.valueOf(index).toString();
        }
        else if (fieldType.equals(JavaType.SHORT_OBJECT)
                || fieldType.equals(JavaType.SHORT_PRIMITIVE)) {
            initializer = Short.valueOf(index).toString();
        }
        else if (fieldType.equals(BIG_DECIMAL)) {
            initializer = new BigDecimal(index).toString();
        }
        return initializer;
    }

    /*
    private void manageTestSuite(final String testPath, final String name,
            final String serverURL) {
        final String relativeTestFilePath = "senchatouch/test-suite.xhtml";
        final String seleniumPath = pathResolver.getFocusedIdentifier(
                Path.SRC_MAIN_WEBAPP, relativeTestFilePath);

        final InputStream inputStream;
        if (fileManager.exists(seleniumPath)) {
            inputStream = fileManager.getInputStream(seleniumPath);
        }
        else {
            inputStream = FileUtils.getInputStream(getClass(),
                    "senchatouch-test-suite-template.xhtml");
            Validate.notNull(inputStream,
                    "Could not acquire senchatouch test suite template");
        }

        final Document suite = XmlUtils.readXml(inputStream);
        final Element root = (Element) suite.getLastChild();

        XmlUtils.findRequiredElement("/html/head/title", root).setTextContent(
                "Test suite for "
                        + projectOperations.getProjectName(projectOperations
                                .getFocusedModuleName()) + "project");

        final Element tr = suite.createElement("tr");
        final Element td = suite.createElement("td");
        tr.appendChild(td);
        final Element a = suite.createElement("a");
        a.setAttribute(
                "href",
                serverURL
                        + projectOperations.getProjectName(projectOperations
                                .getFocusedModuleName()) + "/resources/"
                        + testPath);
        a.setTextContent(name);
        td.appendChild(a);

        XmlUtils.findRequiredElement("/html/body/table", root).appendChild(tr);

        fileManager.createOrUpdateTextFileIfRequired(seleniumPath,
                XmlUtils.nodeToString(suite), false);

        menuOperations.addMenuItem(new JavaSymbolName("SeleniumTests"),
                new JavaSymbolName("Test"), "Test", "selenium_menu_test_suite",
                "/resources/" + relativeTestFilePath, "si_",
                pathResolver.getFocusedPath(Path.SRC_MAIN_WEBAPP));
    }
*/



    private Node verifyTextCommand(final Document document,
            final JavaType formBackingType, final FieldMetadata field) {
        final Node tr = document.createElement("tr");

        final Node td1 = tr.appendChild(document.createElement("td"));
        td1.setTextContent("verifyText");

        final Node td2 = tr.appendChild(document.createElement("td"));
        td2.setTextContent(XmlUtils.convertId("_s_"
                + formBackingType.getFullyQualifiedTypeName() + "_"
                + field.getFieldName().getSymbolName() + "_"
                + field.getFieldName().getSymbolName() + "_id"));

        final Node td3 = tr.appendChild(document.createElement("td"));
        td3.setTextContent(convertToInitializer(field));

        return tr;
    }

}
