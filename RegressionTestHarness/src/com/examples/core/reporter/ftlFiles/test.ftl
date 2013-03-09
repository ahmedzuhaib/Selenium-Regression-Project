<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>Screenshots for test ${name}</title>

<link href="../media/css/custom.css" type="text/css" rel="stylesheet">

<style type="text/css">
<style type="text/css">
.gallerydescfg{
  font-size:14pt;
}

.gallerydesc{
  left:113px!important;
}

.navpanellayer{
  left:8px!important;
  top :-23px!important;
}

.gallerylayer{
 top:26px!important;
}
</style>
<script>
    var varImageArray =new Array(${screenshots?size} - 1);
    var countIs= 0;
    <#list screenshots as screenshot>
        varImageArray[countIs]=new Array(4)
        varImageArray[countIs][0]="${screenshot.image}"
        varImageArray[countIs][1]=""
        varImageArray[countIs][2]=""
        varImageArray[countIs][3]="${screenshot.description}"
        countIs++
    </#list>
</script>
<#include "header.ftl">
<#include "simpleGallery.ftl">
</head>


<body>

<div class="div1">
  <h2>
    <a href="../../../../index.html">Home</a>
  </h2>
</div>

<div class="div2">
	<h1>ScreenShots Results For Test: ${name}</h1>
	<div class="div3"></div>
	<span>${start}</span>
</div>

<hr size="1">

<div id="simplegallery" > </div>
</body>
</html>