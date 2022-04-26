$(document).ready(() => {
    $('#password-addon, #repeat-password-addon').click(function () {
        $('#password-addon, #repeat-password-addon').each(function () {
                $('input[aria-describedby=' + $(this).attr("id") + ']')
                    .attr('type', (_, attr) => attr === "password" ? "text" : "password")
            }
        ).children().children().toggleClass("fa-eye").toggleClass("fa-eye-slash")
    });

    $('#editUser').click(function () {
        $("input[id!='username'], #username[value!='admin']").attr("readonly", false)
        $('select').attr("disabled", false)
        $(this).hide()
        $('#drop-area').hide()
        $('#edit-buttons').removeClass('d-none').children().attr("readonly", false)

        return false
    })

    $('#cancel').click(function () {
        $('input').attr("readonly", true)
        $('select').attr("disabled", true)
        $('#editUser').show()
        $('#drop-area').show()
        $('#edit-buttons').addClass('d-none').children().attr("readonly", true)

        return false
    })
    window.setTimeout(function () {
        $(".alert").alert('close');
    }, 2000);
});