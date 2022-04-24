$(document).ready(() => {
    let dropArea = document.getElementById('drop-area');

    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        dropArea.addEventListener(eventName, preventDefaults, false)
    })

    function preventDefaults(e) {
        e.preventDefault()
        e.stopPropagation()
    }

    dropArea.addEventListener('drop', handleDrop, false)

    function handleDrop(e) {
        let dt = e.dataTransfer
        let files = dt.files
        handleFiles(files)
    }

    function handleFiles(files) {
        ([...files]).forEach(uploadFile)
    }

    function uploadFile(file) {
        let url = 'upload'
        let formData = new FormData()
        formData.append('file', file)
        fetch(url, {
            method: 'POST',
            body: formData
        })
            .then(() => {
                location.reload()
            })
            .catch(() => {
                location.reload()
            })
    }
})