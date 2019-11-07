$(document).ready(function() {
    $('#UploadFile').on('change', function(e) {
            readURL(this,$("#OriginalImg"));
            
            var filename = $(this).val().split('\\').pop();
            var filesize = BytesToKiloBytes(this.files[0].size);
    });
    $('#UploadToEnc').on('change', function(e) {
                    
    });
});
function readURL(input,display) {
    if (input.files && input.files[0]) {
        
        var reader = new FileReader();

        reader.onload = function (e) {
            display.attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
    }
}
function BytesToKiloBytes(input) {
    input /= 1024;
    return (Math.round(input * 100) / 100);
}