<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="${pageContext.request.contextPath}/resources/css/companyInfo.css" rel="stylesheet" >
<script src="${pageContext.request.contextPath}/resources/js/placeSearchBoxGmap.js"></script>
<title>Company Infomation</title>
</head>
<body>
<form>
<label>Company Name</label>
<input id="cName" class="controls" type="tetxt" placeholder="Name"/>


<label>Company Adrress</label>
<input id="pac-input" class="controls" type="text" placeholder="Address"/>

<input id="btn-submit" onclick="redirect('<%=request.getContextPath()%>/managerHomepage.html')" class="button-submit" type="button" value="Submit"/>    
</form>

<div id="map" style="margin-top:5px;"></div>

<script src="https://maps.googleapis.com/maps/api/js?libraries=places&callback=initAutocomplete" async defer></script>
</body>
</html>