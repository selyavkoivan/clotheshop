$(document).ready(() => {

    $('#add-size').click(function () {
        let count = $('.add-size-block').length
        $('.add-size-block').last().after($('.add-size-block').last().clone(true))
        $('.add-size-block').last().children('input').first().val("")
        $('.add-size-block').last().children('input').first().attr("name", "sizes[" + count + "].size")
        $('.add-size-block').last().children('input').last().val("")
        $('.add-size-block').last().children('input').last().attr("name", "sizes[" + count + "].count")
        $('.add-size-block').last().children().last().children().attr("name", count)
        return false
    })

    $('.delete').click(function () {
        let number = $(this).attr("name")
        let elements = $('.add-size-block').eq($(this).attr("name")).nextAll()
        $('.add-size-block').eq($(this).attr("name")).remove()
        elements.each(function (index) {
            let trueIndex = +number + +index
            $(this).children('input').first().attr("name", "sizes[" + trueIndex + "].size")
            $(this).children('input').last().attr("name", "sizes[" + trueIndex + "].count")
            $(this).children().last().children().attr("name", trueIndex)
        })
        return false
    })

    $('#plus-btn').click(function () {
        $('#qty_input').val(parseInt($('#qty_input').val()) + 1);
        if (+$('#qty_input').val() > +$('#qty_input').attr("max")) {
            $('#qty_input').val(parseInt($('#qty_input').val()) - 1);
        } else if ($('#qty_input').val() == 0) $('#qty_input').val(1);
        return false
    });

    $('#minus-btn').click(function () {
        $('#qty_input').val(parseInt($('#qty_input').val()) - 1);
        if ($('#qty_input').val() == 0) {
            $('#qty_input').val(1);
        }
        return false
    });

    $('.size-addon').click(function () {
        $('.size-addon').removeClass("bg-primary")
        $(this).addClass("bg-primary")
        $('#qty_input').attr('max', $(this).attr('name'))
        $('#count').removeClass('d-none').children('small').eq(1).text($(this).attr('name'))
        $('#sizeId').val($(this).attr('id'))
    })

    $('.image').click(function () {
        let modalImg = document.getElementById("img01");
        let modal = document.getElementById("myModal");
        modal.style.display = "block";
        modalImg.src = $(this).children().attr("src");
        $('#minus-btn').hide()
        $('#plus-btn').hide()

        let windowHeightToImageHeightRatio = $(window).height()*0.6 / $('#img01').height()
        let windowWidthToImageWidthRatio = $(window).width()*0.6 / $('#img01').width()

        let coefficientOfCompression = windowHeightToImageHeightRatio < windowWidthToImageWidthRatio ?
            windowHeightToImageHeightRatio : windowWidthToImageWidthRatio

        $('#img01').width($('#img01').width()*coefficientOfCompression)

    })

    $('.close').click(function() {
        let modal = document.getElementById("myModal");
        modal.style.display = "none";
        $('#minus-btn').show()
        $('#plus-btn').show()
    })

    $('#setMainPhoto').click(function () {
        let modalImg = document.getElementById("img01")

        let url = 'setMainPhoto'
        let formData = new FormData()
        formData.append('photoUrl', modalImg.src)
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

    })
})