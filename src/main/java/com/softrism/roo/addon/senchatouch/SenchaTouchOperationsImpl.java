package com.softrism.roo.addon.senchatouch;

import static org.springframework.roo.model.JavaType.LONG_OBJECT;
import static org.springframework.roo.model.JdkJavaType.BIG_DECIMAL;
import static org.springframework.roo.model.Jsr303JavaType.FUTURE;
import static org.springframework.roo.model.Jsr303JavaType.MIN;
import static org.springframework.roo.model.Jsr303JavaType.PAST;
import static org.springframework.roo.model.RooJavaType.ROO_WEB_SCAFFOLD;
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
import org.springframework.roo.classpath.details.FieldMetadataBuilder;
import org.springframework.roo.classpath.details.MemberFindingUtils;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadata;
import org.springframework.roo.classpath.operations.DateTime;
import org.springframework.roo.classpath.persistence.PersistenceMemberLocator;
import org.springframework.roo.classpath.scanner.MemberDetails;
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
    private String SENCHA_APP_BASE =  "phone/";
    private String COMMON_TEMPLATES_LIST = "common-templates.list";
    private String JAVA2JS_MAPPING_FILE =  "java2js-mapping.properties";

    HashMap<String,String> java2JsMapping;

    /*
    Map code
    #foreach( $key in $entity.attrMap.keySet() )
     {
        name: '$key',
        type: '$entity.attrMap.get($key)
      },
    #end
     */
    //<li>Key: $key -> Value: $allProducts.get($key)</li>
    /**
     * Creates Sencha Touch code.
     * 
     * @param serverURL the JavaType of the controller under test (required)
     */
    public void generateSenchaTouchCode(String serverURL) {

        if (!serverURL.endsWith("/")) {
            serverURL = serverURL + "/";   // Here I may want to add app name to the end.
        }

        Properties properties = new Properties();
        try {
            properties.load(SenchaTouchOperationsImpl.class.getClassLoader().getResourceAsStream(JAVA2JS_MAPPING_FILE));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        java2JsMapping= new HashMap<String, String>((Map) properties);

        VelocityEnabler velocityEnabler = new VelocityEnabler();

        AppBean appBean = new AppBean("seleroo");

        ArrayList<EntityBean> allEntities = getAllValidEntities();

        appBean.setEntityList(allEntities);

        InputStream is = SenchaTouchOperationsImpl.class.getClassLoader().getResourceAsStream(COMMON_TEMPLATES_LIST);

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String fileName;
        try {
            while((fileName = br.readLine()) != null){

                String templateFile = TEMPLATE_ROOT + fileName;

                for(EntityBean entityBean : allEntities) {

                    String parsedString = velocityEnabler.velocityExecute(templateFile, appBean, entityBean);
                    createEntityJsFile(entityBean, fileName, parsedString);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String url =  serverURL + projectOperations.getProjectName(projectOperations.getFocusedModuleName()) ;
        System.out.println("url::::::::::::::" + url);

        System.out.println("Ended addon-roo-sencha successfully!..");

    }

    public boolean createEntityJsFile(EntityBean entityBean, String templateName, String fileContent ) {
        String relativeFilePath = SENCHA_APP_BASE + templateName;

        relativeFilePath = relativeFilePath.replaceAll("Entity", entityBean.getName());

        final String finalFilePath = pathResolver.getFocusedIdentifier(
                Path.SRC_MAIN_WEBAPP, relativeFilePath);

        fileManager.createOrUpdateTextFileIfRequired(finalFilePath,
               fileContent , false);

        return true;
    }

    public boolean isSenchaTouchInstallationPossible() {
        return projectOperations.isFocusedProjectAvailable()
                && projectOperations.isFeatureInstalled(FeatureNames.MVC);
    }

    public ArrayList<EntityBean> getAllValidEntities() {

        ArrayList<EntityBean> allEntities = new ArrayList<EntityBean>();

        System.out.println("Scaffolded Controllers found..");
        for (final JavaType type : typeLocationService
                .findTypesWithAnnotation(ROO_WEB_SCAFFOLD)) {
            System.out.println(type.getFullyQualifiedTypeName());
            ClassOrInterfaceTypeDetails controllerTypeDetails = typeLocationService
                    .getTypeDetails(type);
            LogicalPath path = PhysicalTypeIdentifier
                    .getPath(controllerTypeDetails.getDeclaredByMetadataId());
            String webScaffoldMetadataIdentifier = WebScaffoldMetadata
                    .createIdentifier(type, path);
            WebScaffoldMetadata webScaffoldMetadata = (WebScaffoldMetadata) metadataService
                    .get(webScaffoldMetadataIdentifier);
            Validate.notNull(
                    webScaffoldMetadata,
                    "Web controller '%s' does not appear to be an automatic, scaffolded controller",
                    type.getFullyQualifiedTypeName());
            if (!webScaffoldMetadata.getAnnotationValues().isCreate()) {
                LOGGER.warning("The controller you specified does not allow the creation of new instances of the form backing object. No Sencha Touch code created.");
                return null;
            }

            JavaType formBackingType = webScaffoldMetadata
                    .getAnnotationValues().getFormBackingObject();

            EntityBean entityBean = new EntityBean(formBackingType.getSimpleTypeName());
            entityBean.setAttrMap(getEntityMemberDetails(formBackingType));

            allEntities.add(entityBean);

        }
        return allEntities;
    }

    private LinkedHashMap<String,String> getEntityMemberDetails(JavaType formBackingType){

        LinkedHashMap<String,String> fieldMap = new LinkedHashMap<String, String>();

        Validate.notNull(
                formBackingType,
                "Class or interface type details for type '%s' could not be resolved",
                formBackingType);

        final ClassOrInterfaceTypeDetails formBackingTypeDetails = typeLocationService
                .getTypeDetails(formBackingType);
        final MemberDetails memberDetails = memberDetailsScanner
                .getMemberDetails(getClass().getName(), formBackingTypeDetails);

        // Add composite PK identifier fields if needed

        //     Getting all fields
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
                System.out.println("getEmbeddedIdentifierFields: " + fieldName);
            }
        }

        // Add all other fields
        final List<FieldMetadata> fields = webMetadataService
                .getScaffoldEligibleFieldMetadata(formBackingType,
                        memberDetails, null);
        for (final FieldMetadata field : fields) {
            final JavaType fieldType = field.getFieldType();
            if (!fieldType.isCommonCollectionType()
                    && !isSpecialType(fieldType)) {
                System.out.println("Found field " + field.getFieldName().getSymbolName() + " for entity " + formBackingType.getSimpleTypeName());
                fieldMap.put(field.getFieldName().getSymbolName(),java2JsMapping.get(field.getFieldType().getFullyQualifiedTypeName()));
            }
        }

        return fieldMap;
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
