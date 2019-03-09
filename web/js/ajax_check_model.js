var xmlHttp = null;
function isModelExist() {
    value = document.getElementById("name").value;
    if (value == null || value == "") {
        document.getElementById("submitButton").disabled = true;
        document.getElementById("flag").src="images/not_allowed.png";            
        return false;
    }
    createXmlHttpRequest();
    xmlHttp.onreadystatechange = handleUniqueNameRequest;
    xmlHttp.open("POST", "CheckDataServlet?action=4&model=" + value, true);
    xmlHttp.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}

function createXmlHttpRequest() {
    if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else if (window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    }
}

function handleUniqueNameRequest() {
    if (xmlHttp.readyState == 4) {
        xmlvalue = xmlHttp.responseText;
        if (xmlvalue == null) {
            xmlvalue = xmlHttp.responseXML;
        }
        obj = JSON.parse(xmlvalue);
        var results=""+obj.status;
        if (results=="false") {
            document.getElementById("submitButton").disabled = false;
            document.getElementById("flag").src="images/okay.png";
        }else{
            document.getElementById("submitButton").disabled = true;
            document.getElementById("flag").src="images/not_allowed.png";            
        }
    }
}
function showHideSensors(){
    if(document.getElementById("sensorsCount").value==1){
        document.getElementById("second1").style.display='none';
        document.getElementById("second2").style.display='none';
        document.getElementById("second3").style.display='none';
        document.getElementById("second4").style.display='none';
        document.getElementById("second5").style.display='none';
    }else{
        document.getElementById("second1").style.display='';
        document.getElementById("second2").style.display='';
        document.getElementById("second3").style.display='';
        document.getElementById("second4").style.display='';
        document.getElementById("second5").style.display='';
    }
}