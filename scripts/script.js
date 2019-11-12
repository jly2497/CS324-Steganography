$(document).ready(function() {
    $('#UploadFile').on('change', function(e) {
        readURL(this,$("#OriginalImg"));
          
        var filenameOrg = $(this).val().split('\\').pop();
        var filesizeOrg = BytesToKiloBytes(this.files[0].size);
    });
    $('#UploadToEnc').on('change', function(e) {
		readURL(this,$("#EncodeImg"));
		
		var filenameEnc = $(this).val().split('\\').pop();
        var filesizeEnc = BytesToKiloBytes(this.files[0].size);
    });
	$('#Reset').on('click', function(e) {
        $("#OriginalImg").hide();
		$("#EncodeImg").hide();
    });
});
function readURL(input,display) {
    if (input.files && input.files[0]) {
        
        var reader = new FileReader();

        reader.onload = function (e) {
            display.attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
		display.show();
    }
}
function BytesToKiloBytes(input) {
    input /= 1024;
    return (Math.round(input * 100) / 100);
}