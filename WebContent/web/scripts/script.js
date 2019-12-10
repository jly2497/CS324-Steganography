$(document).ready(function() {
	$('#file-container').hide();
	readLog();
	
	$('#Form').on('submit', function(e) {
		//e.preventDefault();

		//var formData = new FormData($(this)[0]);
		
		/*
		$.ajax({
			url: "/Steganography/encode_hdlr",
			type: "post",
			data: formData,
			success: function (msg) {
				alert(msg)
			},
			cache: false,
			contentType: false,
			processData: false
		}).done(function(results) {
			//var time = new Date().getTime();
			//console.log(time);
			$('#OriginalImg').attr('src','web/images/tmp/out.png');
		});*/
	});
	
	$('#ClearLog').on('click', function(e){
		window.location.href = '/Steganography/encode_hdlr?clear=true';
	});
	
	$('#UploadFile').on('change', function(e) {
		//console.log(validType(this));
		if (validType(this)) {
			$('#text-out').hide();
			readURL(this,$("#OriginalImg"));
			$('#Decode').prop("disabled",false);
			$('#OriginalImg').show();
		}
    });
    $('#UploadToEnc').on('change', function(e) {
    	if (validType(this)) {
    		readURL(this,$("#EncodeImg"));
    	}
		
		//var filenameEnc = $(this).val().split('\\').pop();
        //var filesizeEnc = BytesToKiloBytes(this.files[0].size);
    });
	$('#Reset').on('click', function(e) {
        $("#OriginalImg").hide();
		$("#EncodeImg").hide();
		$('#Decode').prop("disabled",true);
    });
	$('#TextOrImage').on('change', function(e) {
		if ($('#TextOrImage').val() === "text") {
			$('#file-container').hide();
			$('#UploadToEnc').prop('required',false);
			$('#text-container').show();
			$('#TextMessage').prop('required',true);
		} else {
			$('#text-container').hide();
			$('#TextMessage').prop('required',false);
			$('#file-container').show();
			$('#UploadToEnc').prop('required',true);
		}
	})
	
	$('#UploadFile').change(validateFileInput);
	$('#UploadToEnc').change(validateFileInput);
	
	function validateFileInput() {
		if ((this.id === "UploadToEnc" && $('#file-container').is(":visible")) || this.id === "UploadFile") {
			var errorMsg = "File size must be below 5 megabytes.";
			var input = this;
			var maxSize = $(input).data('max-size');
			
			if ($(input).get(0).files.length) {
				var fileSize = $(input).get(0).files[0].size;
				var hasError = (maxSize < fileSize);
				
				if (typeof input.setCustomValidity === 'function') {
					input.setCustomValidity(hasError ? errorMsg : '');
					input.reportValidity();
				} else {
					$(input).toggleClass('error', !!hasError);
					$(input).toggleClass('ok', !hasError);
					if (hasError) {
						$(input).attr('title', errorMsg);
					} else {
						$(input).removeAttr('title');
					}
				}
			}
		}
		
	}
	
	$('#TextMessage').keyup(validateTextarea);
	
	function validateTextarea() {
		if ($('#text-container').is(":visible")) {
			var errorMsg = "Text must be between 1 to 500 characters.";
			var textarea = this;
			var pattern = new RegExp('^' + $(textarea).attr('pattern') + '$');
			// check each line of text
			$.each($(this).val().split("\n"), function () {
				// check if the line matches the pattern
				var hasError = !this.match(pattern);
				if (typeof textarea.setCustomValidity === 'function') {
					textarea.setCustomValidity(hasError ? errorMsg : '');
				} else {
					$(textarea).toggleClass('error', !!hasError);
					$(textarea).toggleClass('ok', !hasError);
					if (hasError) {
						$(textarea).attr('title', errorMsg);
					} else {
						$(textarea).removeAttr('title');
					}
				}
				return !hasError;
			});
		}
	}
});
function readLog() {
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", "web/log.txt", false);
    rawFile.onreadystatechange = function ()
    {
        if(rawFile.readyState === 4)
        {
            if(rawFile.status === 200 || rawFile.status == 0)
            {
                var allText = rawFile.responseText;
                $('.log-container textarea').val(allText);
            }
        }
    }
    rawFile.send(null);
}

	
function validType(input) {
	var file = input.files[0];
	var fileType = file["type"];
	var validImageTypes = ["image/gif", "image/jpeg", "image/png"];
	
	if ($.inArray(fileType, validImageTypes) > 0) {
	     return true;
	} else {
		return false;
	}
}
function submitForm() {
	console.log($('#text-container').val());
}
function readURL(input,display) {
    if (input.files && input.files[0]) {
        
        var reader = new FileReader();

        reader.onload = function (e) {
            display.attr('src', e.target.result);
        };

        reader.readAsDataURL(input.files[0]);
        
        console.log(input.files[0]);
        
		display.show();
    }
}