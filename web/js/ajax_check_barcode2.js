var xmlHttp = null;
function isBarCodeCorrect() {
    value = document.getElementById("barcode").value;
    if (value == null || value == "" || value.length<7) {
        document.getElementById("submitButton").disabled = true;
        document.getElementById("flag").src="images/not_allowed.png";            
        return false;
    }
    createXmlHttpRequest();
    xmlHttp.onreadystatechange = handleUniqueNameRequest;
    xmlHttp.open("POST", "CheckDataServlet?action=2&barcode=" + value, true);
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
