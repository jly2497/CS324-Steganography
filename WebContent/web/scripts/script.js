$(document).ready(function() {
	$('#file-container').hide();
	
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
			displayImage(this,$("#OriginalImg"));
			$('#Decode').removeAttr('disabled');
			$('#Download').attr('disabled','disabled');
			$('#OriginalImg').show();
			$('.zoom-buttons button').removeAttr('disabled');
		}
    });
    $('#UploadToEnc').on('change', function(e) {
    	if (validType(this)) {
    		displayImage(this,$("#EncodeImg"));
    	}
		
		//var filenameEnc = $(this).val().split('\\').pop();
        //var filesizeEnc = BytesToKiloBytes(this.files[0].size);
    });
	$('#Reset').on('click', function(e) {
        $("#OriginalImg").hide();
		$("#EncodeImg").hide();
		$('#Decode').attr('disabled','disabled');
		$('#Download').attr('disabled','disabled');
		$('.zoom-buttons button').attr('disabled','disabled');
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
			var errorMsg = "File size must be below 10 megabytes.";
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
	
	$('#TextMessage').keyup(validateTextMessage);
	
	function validateTextMessage() {
		if ($('#text-container').is(":visible")) {
			var errorMessage = "Text must be between 1 to 500 characters.";
			var textMessage = this;
			var regex = new RegExp('^' + $(textMessage).attr('pattern') + '$');
			$.each($(this).val().split("\n"), function () {
				var match = !this.match(regex);
				if (typeof textMessage.setCustomValidity === 'function') {
					textMessage.setCustomValidity(match ? errorMessage : '');
				} else {
					$(textMessage).toggleClass('error', !!match);
					$(textMessage).toggleClass('ok', !match);
					if (match) {
						$(textMessage).attr('title', errorMessage);
					} else {
						$(textMessage).removeAttr('title');
					}
				}
				return !match;
			});
		}
	}
});
function zoomIn() {
    var GFG = document.getElementById("OriginalImg");
    var currWidth = GFG.clientWidth;
    GFG.style.width = (currWidth + 100) + "px";
}
function zoomOut() {
    var GFG = document.getElementById("OriginalImg");
    var currWidth = GFG.clientWidth;
    GFG.style.width = (currWidth - 100) + "px";
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
function displayImage(input,display) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) { display.attr('src', e.target.result); };
        reader.readAsDataURL(input.files[0]);
		display.show();
    }
}