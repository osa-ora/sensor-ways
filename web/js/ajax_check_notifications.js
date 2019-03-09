setInterval(refreshNotifications, 20000);
var xmlHttp2 = null;
function refreshNotifications() {
    createXmlHttpRequest2();
    xmlHttp2.onreadystatechange = handleUniqueNameRequest2;
    xmlHttp2.open("POST", "CheckDataServlet?action=3", true);
    xmlHttp2.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}
function updateNotifications(notificationId) {
    createXmlHttpRequest3();
    xmlHttp2.onreadystatechange = handleUniqueNameRequest3;
    xmlHttp2.open("POST", "CheckDataServlet?action=4&notificationId="+notificationId, true);
    xmlHttp2.send("<?XML version=\"1.0\" encoding=\"UTF-8\"?>");
}

function createXmlHttpRequest2() {
    if (window.ActiveXObject) {
        xmlHttp2 = new ActiveXObject("Microsoft.XMLHTTP");
    } else if (window.XMLHttpRequest) {
        xmlHttp2 = new XMLHttpRequest();
    }
}
function createXmlHttpRequest3() {
    if (window.ActiveXObject) {
        xmlHttp2 = new ActiveXObject("Microsoft.XMLHTTP");
    } else if (window.XMLHttpRequest) {
        xmlHttp2 = new XMLHttpRequest();
    }
}

function handleUniqueNameRequest2() {
    if (xmlHttp2.readyState == 4) {
        xmlvalue = xmlHttp2.responseText;
        if (xmlvalue == null) {
            xmlvalue = xmlHttp2.responseXML;
        }
        obj = JSON.parse(xmlvalue);
        //console.log("Status=" + obj.status);
        document.getElementById("total").innerHTML = '('+obj.status+')';
    }
}
function handleUniqueNameRequest4() {
    if (xmlHttp2.readyState == 4) {
        xmlvalue = xmlHttp2.responseText;
        if (xmlvalue == null) {
            xmlvalue = xmlHttp2.responseXML;
        }
        obj = JSON.parse(xmlvalue);
        //console.log("Status=" + obj.status);
        document.getElementById("total").innerHTML = '('+obj.status+')';
    }
}
