package com.axis2.api;

import net.javacrumbs.json2xml.JsonXmlReader;
import org.xml.sax.InputSource;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

public class Json2Xml {
    public static final String JSON = "{\"document\":{\"a\":1,\"b\":2,\"c\":{\"d\":\"text\"},\"e\":[1,2,3],\"f\":[[1,2,3],[4,5,6]], \"g\":null, " +
            "\"h\":[{\"i\":true,\"j\":false}],\"k\":[[{\"l\":1,\"m\":2}],[{\"n\":3,\"o\":4},{\"p\":5,\"q\":6}]]}}";

    private static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<document>\n" +
            "	<a>1</a>\n" +
            "	<b>2</b>\n" +
            "	<c>\n" +
            "		<d>text</d>\n" +
            "	</c>\n" +
            "	<e>\n" +
            "		<e>1</e>\n" +
            "		<e>2</e>\n" +
            "		<e>3</e>\n" +
            "	</e>\n" +
            "	<f>\n" +
            "		<f>\n" +
            "			<f>1</f>\n" +
            "			<f>2</f>\n" +
            "			<f>3</f>\n" +
            "		</f>\n" +
            "		<f>\n" +
            "			<f>4</f>\n" +
            "			<f>5</f>\n" +
            "			<f>6</f>\n" +
            "		</f>\n" +
            "	</f>\n" +
            "   <g/>\n" +
            "   <h>\n" +
            "       <h>\n" +
            "           <i>true</i>\n" +
            "           <j>false</j>\n" +
            "       </h>\n" +
            "   </h>\n" +
            "   <k>\n" +
            "      <k>\n" +
            "        <k>\n" +
            "            <l>1</l>\n" +
            "            <m>2</m>\n" +
            "        </k>\n" +
            "      </k>\n" +
            "      <k>\n" +
            "        <k>\n" +
            "            <n>3</n>\n" +
            "            <o>4</o>\n" +
            "       </k>\n" +
            "        <k>\n" +
            "            <p>5</p>\n" +
            "            <q>6</q>\n" +
            "       </k>\n" +
            "     </k>\n" +
            "   </k>\n" +
            "</document>\n";

    private static final String XML_WITH_TYPES = "" +
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<document xmlns=\"http://javacrumbs.net/test\">\n" +
            "	<a type=\"int\">1</a>\n" +
            "	<b type=\"int\">2</b>\n" +
            "	<c>\n" +
            "		<d type=\"string\">text</d>\n" +
            "	</c>\n" +
            "	<e type=\"array\">\n" +
            "		<e type=\"int\">1</e>\n" +
            "		<e type=\"int\">2</e>\n" +
            "		<e type=\"int\">3</e>\n" +
            "	</e>\n" +
            "	<f type=\"array\">\n" +
            "		<f type=\"array\">\n" +
            "			<f type=\"int\">1</f>\n" +
            "			<f type=\"int\">2</f>\n" +
            "			<f type=\"int\">3</f>\n" +
            "		</f>\n" +
            "		<f type=\"array\">\n" +
            "			<f type=\"int\">4</f>\n" +
            "			<f type=\"int\">5</f>\n" +
            "			<f type=\"int\">6</f>\n" +
            "		</f>\n" +
            "	</f>\n" +
            "	<g type=\"null\" />\n" +
            "   <h type=\"array\">\n" +
            "       <h>\n" +
            "         <i type=\"boolean\">true</i>\n" +
            "         <j type=\"boolean\">false</j>\n" +
            "       </h>\n" +
            "   </h>\n" +
            "   <k type=\"array\">\n" +
            "      <k type=\"array\">\n" +
            "         <k>\n" +
            "            <l type=\"int\">1</l>\n" +
            "            <m type=\"int\">2</m>\n" +
            "        </k>\n" +
            "      </k>\n" +
            "      <k type=\"array\">\n" +
            "        <k>\n" +
            "            <n type=\"int\">3</n>\n" +
            "            <o type=\"int\">4</o>\n" +
            "        </k>\n" +
            "        <k>\n" +
            "            <p type=\"int\">5</p>\n" +
            "            <q type=\"int\">6</q>\n" +
            "       </k>\n" +
            "      </k>\n" +
            "   </k>\n" +
            "</document>";

    static final String myJson = "{\n" +
            "    \"request\": {\n" +
            "        \"Body\": {\n" +
            "            \"getOpResultForPackage\": {\n" +
            "                \"areanum\": \"2\",\n" +
            "                \"currentPage\": \"1\",\n" +
            "                \"pageNum\": \"10\"\n" +
            "            }\n" +
            "        },\n" +
            "        \"Header\": \" \"\n" +
            "    }\n" +
            "}";

    public static void main(String args0 []) throws Exception {
        String result = json2xml(myJson,new JsonXmlReader());
        System.out.println(result);
    }

        public static String json2xml(final String json, final JsonXmlReader reader) throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            InputSource source = new InputSource(new StringReader(json));
            Result result = new StreamResult(out);
            transformer.transform(new SAXSource(reader, source), result);
            String str = new String(out.toByteArray());
            return str;
        }
}
