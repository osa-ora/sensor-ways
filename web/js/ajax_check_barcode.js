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
            document.getElementById("submitButton").disabled = true;
            document.getElementById("flag").src="images/not_allowed.png";            
        }else{
            var names=results.split("#");
            document.getElementById("submitButton").disabled = false;
            document.getElementById("flag").src="images/okay.png";
            document.getElementById("model").value=names[1];
            if(names[2]!='null') {
                document.getElementById("high_alert1").value=names[2];
                document.getElementById("high_alert1").disabled=false;                
            } else {
                document.getElementById("high_alert1").value="";
                document.getElementById("high_alert1").disabled=true;
            }
            if(names[3]!='null') {
                document.getElementById("high_alert2").value=names[3];
                document.getElementById("high_alert2").disabled=false;                
            } else {
                document.getElementById("high_alert2").value="";
                document.getElementById("high_alert2").disabled=true;
            }
            if(names[4]!='null') {
                document.getElementById("low_alert1").value=names[4];
                document.getElementById("low_alert1").disabled=false;                
            } else {
                document.getElementById("low_alert1").value="";
                document.getElementById("low_alert1").disabled=true;
            }
            if(names[5]!='null') {
                document.getElementById("low_alert2").value=names[5];
                document.getElementById("low_alert2").disabled=false;
            } else {
                document.getElementById("low_alert2").value="";
                document.getElementById("low_alert2").disabled=true;
            }
            validateSeclection(names[0]);
        }
    }
}
function validateSeclection(modelId) {
    if (modelId < 10000) {
        document.forms[0].device1Name.disabled = true;
        document.forms[0].suggested.disabled = true;
        document.forms[0].device1Name.value = '';
        document.forms[0].device2Name.disabled = true;
        document.forms[0].suggested2.disabled = true;
        document.forms[0].device2Name.value = '';
    } else if (modelId > 10000 && modelId < 20000) {
        document.forms[0].device1Name.disabled = false;
        document.forms[0].suggested.disabled = false;
        document.forms[0].device2Name.disabled = true;
        document.forms[0].suggested2.disabled = true;
        document.forms[0].device2Name.value = '';
    } else if (modelId > 20000) {
        document.forms[0].device1Name.disabled = false;
        document.forms[0].suggested.disabled = false;
        document.forms[0].device2Name.disabled = false;
        document.forms[0].suggested2.disabled = false;
    }
}
