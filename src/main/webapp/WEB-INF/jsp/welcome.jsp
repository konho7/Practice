<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="home">
    <h2><fmt:message key="Hello"/></h2>
    <p>Hello Geonho</p>
    <div class="row">
        <div class="col-md-12">
            <spring:url value="/resources/images/pets.png" htmlEscape="true" var="petsImage"/>
            <img class="img-responsive" alt="A cat and a dog" src="${petsImage}"/>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <spring:url value="/resources/images/puppy.jpg" htmlEscape="true" var="puppyImage"/>
            <img class="img-responsive" alt="A happy puppy" src="${puppyImage}"/>
        </div>
    </div>
</petclinic:layout>
