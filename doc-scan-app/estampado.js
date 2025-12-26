const { app, ipcRenderer } = require('electron');
var Clipper = require('image-clipper');

let btnFirmarArchivo;
let imagenDoc;
let posicionFirma;

var documento;
var datosFirma;
var x1;
var y1;
var x;
var y;
var px;
var py;

window.onload = function() {
    posicionFirma = document.getElementById("posicionFirma")

    btnFirmarArchivo = document.getElementById("btnFirmarArchivo")
    btnFirmarArchivo.addEventListener("click", firmarDocumento)
}

ipcRenderer.on('firmaDocumento', function(event, doc, firma) {
    datosFirma = firma;
    documento = doc;
    src = documento[0].fileName;
    imagenDoc = document.getElementById("imagenDoc")
    imagenDoc.addEventListener("click", seleccionarArea)
    imagenDoc.src = src;

});

function seleccionarArea(event) {
    bounds = this.getBoundingClientRect();
    console.log('bounds: ' + bounds)
    var left = bounds.left;
    var top = bounds.top;
    x = event.clientX;
    y = this.clientHeight - event.clientY + top;
    var cw = this.clientWidth
    var ch = this.clientHeight
    var iw = this.naturalWidth
    var ih = this.naturalHeight
    px = x / cw * iw
    py = y / ch * ih

    console.log('left: ' + left)
    console.log('top: ' + top)

    console.log('event.pageY: ' + event.pageY)
    console.log(' this.clientWidth: ' + cw)
    console.log(' this.clientHeight: ' + ch)
    console.log('naturalWidth: ' + ch)
    console.log('naturalWidth: ' + iw)
    console.log('naturalHeight: ' + ih)
    console.log('px: ' + px)
    console.log('py: ' + py)



    var xTemp = event.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
    var yTemp = event.clientY + document.body.scrollTop + document.documentElement.scrollTop;

    console.log('document.body.scrollTop: ' + document.body.scrollTop)
    console.log('document.documentElement.scrollTop: ' + document.documentElement.scrollTop)


    console.log('xTemp: ' + xTemp)
    console.log('yTemp: ' + yTemp)


    x1 = event.pageX - this.offsetLeft;

    y1 = event.pageY - this.offsetTop;
    console.log('this.offsetTop: ' + this.offsetTop)

    if (document.contains(document.getElementById("boxFirma"))) {
        document.getElementById("boxFirma").remove();
    }

    var margin = y1 + "px " + x1 + "px " + y1 + "px " + x1 + "px "
        //   console.log(margin)
    var div = document.createElement("div");
    div.id = 'boxFirma';
    div.style.width = "30px";
    div.style.height = "30px";
    div.style.background = "blue";
    div.style.color = "blue";
    div.style.margin = margin;
    div.style.position = "absolute";
    div.style.border = "1px solid captiontext";

    posicionFirma.appendChild(div);


    console.log("x1 " + x1 + " y1 " + y1 + " mouse pos (" + x + "," + y + ") relative to boundingClientRect at (" + left + "," + top + ") client image size: " + cw + " x " + ch + " natural image size: " + iw + " x " + ih);
};



async function firmarDocumento() {

    const posicionFirma = {
        posicionX: x,
        posicionY: y,
    }

    ipcRenderer.invoke("firmarDocumento", datosFirma, posicionFirma)

}