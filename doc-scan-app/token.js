const { ipcRenderer } = require('electron')


let dropdownTokens;
let listDocuments;
let txtRazon;
let txtPagina;
let txtUbicacion
let btnFirmarArchivo;
let btnUpdate;
let btnform;
let btnLimpiar;
let btnCargarArchivo;
var list;
var usuario;

let pathClave;
let pathArchivo;


window.onload = function() {

    txtRazon = document.getElementById("txtRazon")
    txtPagina = document.getElementById("txtPagina")
    txtUbicacion = document.getElementById("txtUbicacion")

    btnFirmarArchivo = document.getElementById("btnFirmarDocumento")
    btnUpdate = document.getElementById("btnRefresh")
    btnLimpiar = document.getElementById("btnLimpiar")
    btnCargarArchivo = document.getElementById("btnCargarArchivo")


    dropdownTokens = document.getElementById("dropdownTokens")
    pathClave = document.getElementById("pathClave")
    pathArchivo = document.getElementById("pathArchivo")
    listDocuments = document.getElementById("listDocuments")


    btnFirmarArchivo.addEventListener("click", firmarDocumento)
        //s  btnUpdate.onclick = renderGetDocumentos;
    btnLimpiar.onclick = limpiarVariableArchivo;
    btnCargarArchivo.onclick = cargarArchivo;

    getUsuer()
        //renderGetDocumentos()
    renderGetTokens()
};


async function firmarDocumento() {

    var tokenAlias = dropdownTokens.options[dropdownTokens.selectedIndex].value;
    console.log(tokenAlias);

    const obj = {
        archivo: pathArchivo.value,
        token: tokenAlias,
        pagina: txtPagina.value,
        usuario: usuario.usuario,
        usuarioNombre: usuario.nombres,
        usuarioId: usuario.id,
        ubicacion: txtUbicacion.value,
        tipo: "TOKEN"
    }

    ipcRenderer.invoke("generarImagenDocumento", obj, 'TOKEN')

}

async function limpiarVariableArchivo() {
    pathArchivo.value = '';
}

async function cargarArchivo() {
    await ipcRenderer.invoke('abrirDlgExplorador', 'TOKEN')

}

async function renderGetDocumentos() {
    await ipcRenderer.invoke('get', 'TOKEN')
}

async function renderGetTokens() {
    await ipcRenderer.invoke('getTokens')
}


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
    await ipcRenderer.invoke('getUsuario', 'TOKEN')
}


ipcRenderer.on('usuario', (event, results) => {
    usuario = results
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


ipcRenderer.on('tokens', (event, results) => {
    let template = `<option value="SELECCIONE">SELECCIONE</option>`
    list = results
    list.forEach(element => {
        template += `<option value="${element.alias}">${element.alias}</option>`
    });

    dropdownTokens.innerHTML = template
        /*btnform = document.querySelectorAll(".btn-info")
        btnform.forEach(boton => {
           boton.addEventListener("click", renderFirmarDocumento)
        })*/
});


ipcRenderer.on('archivoSeleccionado', (event, result) => {
    pathArchivo.value = result
});




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