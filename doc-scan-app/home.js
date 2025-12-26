const { app, ipcRenderer } = require('electron')

let btnFirmarArchivo;
let btnFirmarToken;
let btnEscanerarDocumentos;
let lblBienvenido;
var usr;

window.onload = function() {
    getUsuer()
    btnFirmarArchivo = document.getElementById("btnFirmarArchivo")
    btnFirmarToken = document.getElementById("btnFirmarToken")
    btnEscanerarDocumentos = document.getElementById("btnEscanerarDocumentos")
    lblBienvenido = document.getElementById("txtLabel")
    btnFirmarArchivo.onclick = function() {
        ipcRenderer.invoke("firmaFrame")
    }
    btnFirmarToken.onclick = function() {
        ipcRenderer.invoke("tokenFrame")
    }
    btnEscanerarDocumentos.onclick = function() {
        ipcRenderer.invoke("documentsScan")
    }
}

async function getUsuer() {
    await ipcRenderer.invoke('getUsuario', 'INICIO')

}

ipcRenderer.on('usuario', (event, results) => {
    console.log('results')
    console.log(results)
    usr = results
    lblBienvenido.value = 'Bienvenido @' + results;
});