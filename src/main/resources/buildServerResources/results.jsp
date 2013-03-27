<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- GOOD_RESPONSE -->

<span class="resultsTime"><fmt:formatDate value="${now}" pattern="EEE dd-MMM-yyyy HH:mm:ss"/></span>

<c:forEach items="${results}" var="project" varStatus="projectStatus">
    <div class="project">
        <div class="projectName"><c:out value="${project.projectName}"/></div>
        <c:if test="${!(empty project.redBuilds) || (dontShowGreenBuilds && empty project.redBuilds)}">
            <div class="fails">
                <c:forEach items="${project.redBuilds}" var="build" varStatus="status">
                    <span class="<c:out value='${build.lastFinished.status} ${build.runningBuild.status}'/> <c:if test='${runningInItalics}'>italics</c:if> <c:if test='${blink}'>blink</c:if>">
                        <c:out value="${build.buildName}"/>
                        <c:if test="${showTimeSinceFirstFail and !(empty build.lastFinished.timeSinceFirstFailure)}">
                            <span class="lastGoodBuildDate">(<c:out value="${build.lastFinished.timeSinceFirstFailure}"/>)</span>
                        </c:if>
                        <c:if test="${showTimeRemaining and !(empty build.runningBuild.timeRemaining)}">
                            <span class="timeRemaining <c:out value="${build.runningBuild.status}"/>">
                                (<c:out value="${build.runningBuild.timeRemaining}"/>)
                            </span>
                        </c:if>
                    </span>
                    <c:if test="${build.responsibilityAssigned}"> &#174; </c:if>
                    <c:if test="${!status.last}">
                        <c:choose>
                            <c:when test="${runTogether}">, </c:when>
                            <c:otherwise><br/></c:otherwise>
                        </c:choose>
                    </c:if>
                </c:forEach>
                <c:if test="${dontShowGreenBuilds && empty project.redBuilds}">
                    <span class="ok">OK</span>
                </c:if>
            </div>
        </c:if>
        <c:if test="${!(dontShowGreenBuilds || empty project.greenBuilds)}">
            <div>
                <c:forEach items="${project.greenBuilds}" var="build" varStatus="status">
                    <span class="<c:out value="${build.lastFinished.status} ${build.runningBuild.status}"/> <c:if test="${runningInItalics}">italics</c:if>">
                            <c:out value="${build.buildName}"/>
                        <c:if test="${showTimeRemaining and !(empty build.runningBuild.timeRemaining)}">
                            (<c:out value="${build.runningBuild.timeRemaining}"/>)
                        </c:if>
                    </span>
                    <c:if test="${!status.last}">, </c:if>
                </c:forEach>
            </div>
        </c:if>
    </div>
    <c:if test="${!(projectStatus.last) && showDividers}">
        <hr/>
    </c:if>
</c:forEach>


