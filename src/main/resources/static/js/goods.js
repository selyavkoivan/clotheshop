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

})