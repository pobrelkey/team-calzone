<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!-- GOOD_RESPONSE -->


<span class="resultsTime"><fmt:formatDate value="${now}" pattern="EEE dd-MMM-yyyy HH:mm:ss" /></span>

<c:forEach items="${results}" var="project" varStatus="projectStatus">
    <div class="project">
        <div class="projectName"><c:out value="${project.projectName}" /></div>
        <c:if test="${!(empty project.redBuilds) || (dontShowGreenBuilds && empty project.redBuilds)}">
            <div class="fails">
                <c:forEach items="${project.redBuilds}" var="build" varStatus="status">
                    <span class="<c:choose><c:when test="${build.compileFailure}">compileFail</c:when><c:otherwise>fail</c:otherwise></c:choose> <c:if test="${build.active}">active</c:if>"><c:out value="${build.buildName}" /><c:if test="${showTimeRemaining and !(empty build.timeRemaining)}"><span class="<c:choose><c:when test="${build.failing}">fail</c:when><c:otherwise>pass</c:otherwise></c:choose>"> (<c:out value="${build.timeRemaining}" />)</span></c:if></span><c:if test="${!(empty build.responsibility)}"> &#174;</c:if><c:if test="${!status.last}"><c:choose><c:when test="${runTogether}">,</c:when><c:otherwise><br /></c:otherwise></c:choose></c:if>
                </c:forEach>
                <c:if test="${dontShowGreenBuilds && empty project.redBuilds}">
                    <span class="ok">OK</span>
                </c:if>
            </div>
        </c:if>
        <c:if test="${!(dontShowGreenBuilds || empty project.greenBuilds)}">
            <div class="passes">
                <c:forEach items="${project.greenBuilds}" var="build" varStatus="status">
                    <span class="pass <c:if test="${build.active}">active</c:if>"><c:out value="${build.buildName}" /><c:if test="${showTimeRemaining and !(empty build.timeRemaining)}"> (<c:out value="${build.timeRemaining}" />)</c:if></span><c:if test="${!status.last}">,</c:if>
                </c:forEach>
            </div>
        </c:if>
    </div>
    <c:if test="${!(projectStatus.last) && showDividers}">
        <hr />
    </c:if>
</c:forEach>


