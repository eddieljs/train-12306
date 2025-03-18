package com.eddie.train.generator.gen;


import com.eddie.train.generator.util.DbUtil;
import com.eddie.train.generator.util.Field;
import com.eddie.train.generator.util.FreemarkerUtil;
import freemarker.template.TemplateException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ServerGenerator {
    static boolean readOnly = false;
    static String vuePath = "train-admin/src/views/main/";
    static String serverPath="[module1]/src/main/java/com/eddie/train/[module2]/";
    static String pomPath="generator\\pom.xml";

    static String module1 = "";//train-xxx
    static String module2 = "";//xxx

    public static void main(String[] args) throws Exception {
        String generatorPath= getGeneratorPath();
        //根据获取的路径名提取出模块名
        module1 = "train-" + generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        module2 = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        //将路径中的module替换为真正的模块名
        serverPath= serverPath.replace("[module1]",module1);
        serverPath= serverPath.replace("[module2]",module2);
        System.out.println(serverPath);
        //根据getGeneratorPath方法读取xml文件
        Document document = new SAXReader().read("generator/" + generatorPath);
        //读取table节点
        Node table = document.selectSingleNode("//table");
        System.out.println(table);
        //根据table节点查属性 tableName表名 domainObjectName实体名
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText()+"/"+domainObjectName.getText());
        // 为DbUtil设置数据源
        Node connectionURL = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        System.out.println("url: " + connectionURL.getText());
        System.out.println("user: " + userId.getText());
        System.out.println("password: " + password.getText());
        DbUtil.url = connectionURL.getText();
        DbUtil.user = userId.getText();
        DbUtil.password = password.getText();

        // 示例：表名 jiawa_test
        // Domain = JiawaTest
        String Domain = domainObjectName.getText();
        // domain = jiawaTest
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // do_main = jiawa-test
        String do_main = tableName.getText().replaceAll("_", "-");
        //  表中文名
        String tableNameCn = DbUtil.getTableComment(tableName.getText());
        List<Field> fieldList = DbUtil.getColumnByTableName(tableName.getText());
        Set<String> typeSet = getJavaTypes(fieldList);

        // 组装参数
        Map<String, Object> param = new HashMap<>();
        param.put("module1", module1);
        param.put("module2", module2);
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        param.put("tableNameCn", tableNameCn);
        param.put("fieldList", fieldList);
        param.put("typeSet", typeSet);
        param.put("readOnly", readOnly);
        System.out.println("组装参数：" + param);
        //开始生成
//        gen(Domain,param,"service","service");
//        gen(Domain,param,"controller","controller");
//        gen(Domain,param,"req","saveReq");
//        gen(Domain,param,"req","queryReq");
//        gen(Domain,param,"resp","queryResp");
//        gen(Domain,param,"controller/admin","adminController");
//        gen(Domain,param,"service/serviceImpl","serviceImpl");
        genVue(do_main, param);

    }

    private static void gen(String Domain, Map<String, Object> param, String packageName, String target) throws IOException, TemplateException {
        FreemarkerUtil.initConfig(target + ".ftl");
        //获取生成路径
        String toPath = serverPath + packageName + "/";
        //没有则创建
        new File(toPath).mkdirs();
        //获取生成文件类型
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);
        //拼接文件名
        String fileName = toPath + Domain + Target + ".java";
        System.out.println("开始生成：" + fileName);
        FreemarkerUtil.generator(fileName, param);
    }

    private static void genVue(String do_main, Map<String, Object> param) throws IOException, TemplateException {
        FreemarkerUtil.initConfig("vue.ftl");
        new File(vuePath).mkdirs();
        String fileName = vuePath + do_main + ".vue";
        System.out.println("开始生成：" + fileName);
        FreemarkerUtil.generator(fileName, param);
    }

    /**
     * 读取pom中的configurationFile路径方法
     * @return
     * @throws DocumentException
     */
    private static String getGeneratorPath() throws DocumentException {
        SAXReader saxReader= new SAXReader();
        HashMap<String, String> map = new HashMap<>();
        map.put("pom","http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        return node.getText();
    }

    /**
     * 获取所有的Java类型，使用Set去重
     */
    private static Set<String> getJavaTypes(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            set.add(field.getJavaType());
        }
        return set;
    }
}
