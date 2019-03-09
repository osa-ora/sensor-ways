var xmlHttp = null;
function isUniqueEmail() {
    value = document.getElementById("emailAddress").value;
    if (value == null || value == "" || value.length<7 || !value.includes("@")) {
        document.getElementById("submitButton").disabled = true;
        document.getElementById("flag").src="images/not_allowed.png";            
        return false;
    }
    createXmlHttpRequest();
    xmlHttp.onreadystatechange = handleUniqueNameRequest;
    xmlHttp.open("POST", "CheckDataServlet?action=1&email=" + value, true);
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
        console.log("Status=" + obj.status);
        //document.getElementById("output").innerHTML = xmlvalue;
        if (obj.status) {
            document.getElementById("submitButton").disabled = false;
            document.getElementById("flag").src="images/okay.png";
        }else{
            document.getElementById("submitButton").disabled = true;
            document.getElementById("flag").src="images/not_allowed.png";            
        }
    }
}
