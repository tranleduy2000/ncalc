package com.duy.tools;

import com.duy.lambda.BiConsumer;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;
import static org.apache.commons.io.filefilter.TrueFileFilter.TRUE;

public class DocumentIndexGenerator {


    @Test
    public void testCopyProgrammingDocument() throws Exception {
        System.out.println("DocumentGeneratorTest.copyProgrammingDocument");
        System.out.println(new File(".").getCanonicalPath());

        final File assetsProgrammingDocuments = new File("./src/main/assets/doc").getCanonicalFile();
        assertTrue(assetsProgrammingDocuments.exists());
        final File indexFile = new File(assetsProgrammingDocuments, "help_functions_md_index.json").getCanonicalFile();

        // Post processor
        for (File file : FileUtils.listFiles(assetsProgrammingDocuments, TRUE, TRUE)) {
            String content = FileUtils.readFileToString(file);
            content = content.replace("...", "…");
            FileUtils.write(file, content);
        }

        System.out.println("Create help_functions_md_index.json");
        // createIndex
        {
            JSONObject jsonObject = new JSONObject();
            BiConsumer<JSONObject, File> listFileTree = new BiConsumer<JSONObject, File>() {
                @Override
                public void accept(JSONObject parentDir, File file) {
                    try {
                        if (file.isDirectory()) {
                            JSONObject subDir = new JSONObject();
                            subDir.put("name", file.getName());
                            if (!parentDir.has("children")) {
                                parentDir.put("children", new JSONArray());
                            }
                            parentDir.getJSONArray("children").put(subDir);

                            for (File listFile : file.listFiles()) {
                                accept(subDir, listFile);
                            }
                        } else if (file.getName().endsWith(".md")) {
                            if (!parentDir.has("children")) {
                                parentDir.put("children", new JSONArray());
                            }

                            JSONObject jsonFile = new JSONObject();
                            jsonFile.put("name", file.getName());

                            String content = FileUtils.readFileToString(file);
                            // example: > returns all integers that divide the integer `n`.
                            Matcher matcher = Pattern.compile("[^>]> (.*?)[\n\r]").matcher(content);
                            if (matcher.find()) {
                                String group = matcher.group(1);
                                // check only contains one description line, if the file has one or
                                // more description, it can not be a function document
                                if (!matcher.find()) {
                                    jsonFile.put("desc", group.trim());
                                } else {
                                    System.out.println(matcher.group(1));
                                }
                            }
                            parentDir.getJSONArray("children").put(jsonFile);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            listFileTree.accept(jsonObject, assetsProgrammingDocuments);

            String content = jsonObject.toString(2);
            FileUtils.write(indexFile, content);
        }
    }
}
