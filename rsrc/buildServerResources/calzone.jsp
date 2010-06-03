<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Calzone</title>
    <link rel="stylesheet" type="text/css" href="/plugins/calzone/calzone.css"/>
    <script type="text/javascript" src="/plugins/calzone/prototype.js"></script>
    <script type="text/javascript" src="/plugins/calzone/calzone.js"></script>
    <style type="text/css">
        <c:if test="${blink}">
            .compileFail {
                text-decoration: blink;
            }
        </c:if>
        .fails {
            font-size: <c:out value="${failFontSize}"/>;
        }
    </style>
</head>
<body onload="refreshData()">

<div id="content">
    <%@ include file="results.jsp" %>
</div>
<div id="oops" style="display: none">
    <div id="oopsWarning">Error contacting build server!</div>
    <div id="oopsContent">
        <%@ include file="results.jsp" %>
    </div>
</div>

<div id="bottom">
    <div id="bottomTabDiv">
        <span id="bottomTab" onClick="showHideSettings()">Settings...</span>
    </div>
    <div id="bottomForm" style="display:none">
        <form:form method="get" commandName="formModel">
            <table>
                <tr>
                    <td align="right"><label for="projects">Projects</label>&nbsp;&nbsp;</td>
                    <td><form:select id="projects" path="projectsToDisplay" size="12" items="${projectNames}" />&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td align="right"><label for="builds">Builds</label>&nbsp;&nbsp;</td>
                    <td><form:select id="builds" path="buildsToDisplay" size="12" items="${buildNames}" />&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td>
                        <p>
                            <label for="failFontSize">Failure Font Size:</label>
                            <form:select id="failFontSize" path="failFontSize" items="${failFontSizes}" />
                        </p>
                        <p>
                            <form:checkbox id="dontShowGreenBuilds" path="dontShowGreenBuilds"/>
                            <label for="dontShowGreenBuilds">Don't Show Green Builds</label>
                        </p>
                        <p>
                            <form:checkbox id="showDividers" path="showDividers"/>
                            <label for="showDividers">Dividing Lines Between Projects</label>
                        </p>
                        <p>
                            <form:checkbox id="runTogether" path="runTogether"/>
                            <label for="runTogether">Commas Not Newlines</label>
                        </p>
                        <p>
                            <form:checkbox id="showTimeRemaining" path="showTimeRemaining"/>
                            <label for="showTimeRemaining">Show Time Remaining</label>
                        </p>
                        <p>
                            <form:checkbox id="pendingInItalics" path="pendingInItalics"/>
                            <label for="pendingInItalics">Pending Builds In Italics</label>
                        </p>
                        <p>
                            <form:checkbox id="runningInItalics" path="runningInItalics"/>
                            <label for="runningInItalics">Running Builds In Italics</label>
                        </p>
                        <p>
                            <form:checkbox id="blink" path="blink"/>
                            <label for="blink">Make Compile Failures <span style="text-decoration: blink">BLINK!</span></label>
                        </p>
                        <p>
                            <input type="submit" value="Refresh" />
                        </p>
                    </td>
                </tr>
            </table>
        </form:form>
    </div>
</div>

</body>
</html>