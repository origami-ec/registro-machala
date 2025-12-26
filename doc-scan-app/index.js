const { app, ipcRenderer } = require('electron')
const ProgressBar = require('electron-progressbar');

const pathFile = 'C:\\archivos\\firmasElectronicas\\';
let listDocuments;
let btnUpdate;
let btnform;
var list;
var usuario;

let pathFirma;
let pathClave;
let pathArchivo;

let btnFirmarArchivo;
let btnLimpiar;
let btnCargarArchivo;

window.onload = function() {

    pathFirma = document.getElementById("pathFirma")
    pathClave = document.getElementById("pathClave")
    pathArchivo = document.getElementById("pathArchivo")
    listDocuments = document.getElementById("listDocuments")

    btnFirmarArchivo = document.getElementById("btnFirmarDocumento")
    btnUpdate = document.getElementById("btnRefresh")
    btnLimpiar = document.getElementById("btnLimpiar")
    btnCargarArchivo = document.getElementById("btnCargarArchivo")

    // btnUpdate.onclick = renderGetDocumentos;
    btnLimpiar.onclick = limpiarVariableArchivo;
    btnCargarArchivo.onclick = cargarArchivo;
    btnFirmarArchivo.addEventListener("click", firmarDocumento)
    getUsuer()
        // renderGetDocumentos()

};

async function cargarArchivo() {
    await ipcRenderer.invoke('abrirDlgExplorador', 'FIRMA_ARCHIVO')

}

async function renderGetDocumentos() {
    await ipcRenderer.invoke('get', 'FIRMA_ARCHIVO')
}
ipcRenderer.on('archivoSeleccionado', (event, result) => {
    pathArchivo.value = result
});


async function renderFirmarDocumento(e) {
    // e.target.value.style.display = 'none';
    var document = {}
    for (let doc of list) {
        if (doc.id == e.target.value) {
            document = doc;
            break;
        }
    }

    document.archivo = pathFirma.value;
    document.clave = pathClave.value;
    await ipcRenderer.invoke('firmarDoc', document).then((result) => {
        console.log(result)
            // e.target.style.display = 'block';
    })

}


async function getUsuer() {
    await ipcRenderer.invoke('getUsuario', 'FIRMA_ARCHIVO')
}


ipcRenderer.on('usuario', (event, results) => {
    usuario = results
    pathFirma.value = pathFile.concat(usuario.usuario, '.p12');
});

ipcRenderer.on('documents', (event, results) => {
    let template = ""
    list = results
    list.forEach(element => {
        var fecha = new Date(parseInt(element.fecha)).toLocaleString()
        let e = element;
        template += `
         <tr>
            <td>${element.numTramite}</td>
            <td>${element.motivo}</td>
            <td>${fecha}</td> 
             <td>
               <button class="btn btn-info"   
                 id="btnform"
                 value="${element.id}"> 
                Firmar documento
              </button>
           
            </td>
         </tr>
      `
    });

    listDocuments.innerHTML = template
    btnform = document.querySelectorAll(".btn-info")
    btnform.forEach(boton => {
        boton.addEventListener("click", renderFirmarDocumento)

    })

});


async function limpiarVariableArchivo() {
    pathArchivo.value = '';
}



async function firmarDocumento() {


    const obj = {
        archivo: pathArchivo.value,
        token: pathFirma.value,
        clave: pathClave.value,
        pagina: txtPagina.value,
        usuario: usuario.usuario,
        usuarioNombre: usuario.nombres,
        usuarioId: usuario.id,
        ubicacion: txtUbicacion.value,
        tipo: "ARCHIVO"
    }

    ipcRenderer.invoke("generarImagenDocumento", obj, 'ARCHIVO')

}

document.addEventListener('drop', (event) => {
    event.preventDefault();
    event.stopPropagation();

    for (const f of event.dataTransfer.files) {
        // Using the path attribute to get absolute file path
        console.log('File Path of dragged files: ', f.path)
        if (isEmpty(pathArchivo.value)) {
            pathArchivo.value = "  " + f.path
        } else if (pathArchivo.value != f.path) {
            pathArchivo.value = "  " + f.path
        }

    }
});
document.addEventListener('dragover', (e) => {
    e.preventDefault();
    e.stopPropagation();
});

document.addEventListener('dragenter', (event) => {
    console.log('File is in the Drop Space');
});

document.addEventListener('dragleave', (event) => {
    console.log('File has left the Drop Space');
});

function isEmpty(value) {
    return (value == null || value.length === 0 || value === '');
}