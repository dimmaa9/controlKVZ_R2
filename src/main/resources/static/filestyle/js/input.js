$('#input01').jfilestyle()

$('#input02').jfilestyle({
    text : 'My filestyle',
    dragdrop: false
});

$('#input03').jfilestyle({
    input : false
});
$('#input04').jfilestyle({
    onChange : function (e) {
        alert(e)
    }
});

$('#input06').jfilestyle({
    inputSize : '100px'
});

$('#input07').jfilestyle({
    placeholder: 'My text placeholder'
});
$('#input08').jfilestyle();

$('#clear').click(function() {
    $('#input08').jfilestyle('clear');
});

$('#input09').jfilestyle();

$('#toggleInput').click(function() {
    var fs = $('#input09');
    if (fs.jfilestyle('input'))
        fs.jfilestyle('input', false);
    else
        fs.jfilestyle('input', true);
});

$('#input10').jfilestyle();

$('#theme').click(function() {
    var fs = $('#input10');
    if (fs.jfilestyle('theme') == 'default') {
        $(this).html('Default theme');
        fs.jfilestyle('theme', 'blue');
    } else {
        $(this).html('Blue theme');
        fs.jfilestyle('theme', 'default');
    }
});


$('#input12').jfilestyle({
    theme : 'red'
});

$('#input13').jfilestyle();
$('#input14').jfilestyle({disabled: true});
$('#input15').jfilestyle({buttonBefore: true});

$('#destroy').click(function() {
    if ($('#input12').data('jfilestyle')) {
        $('#input12').jfilestyle('destroy');
        $(this).html('Construct');
    } else {
        $('#input12').jfilestyle();
        $(this).html('Destroy');
    }
});

$('.test').jfilestyle();