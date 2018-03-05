import "../lib/quill/quill.snow.css";
import Quill from "../lib/quill/quill";
import Delta from "quill-delta";

(function ($) {
    $.fn.quill = function () {
        let component = this;

        let htmlContent = $(component).find("input.html-content");
        let quillContent = $(component).find("input.quill-content");
        let quillImageFile = $(component).find("input.quill-image");
        let quillEditor = $(component).find(".component .quill-container .quill-editor").get(0);

        let toolbarOptions = [
            ['bold', 'italic', 'underline', 'strike'],
            [{'color': []}, {'background': []}],
            [{'indent': '-1'}, {'indent': '+1'}],
            [{'align': []}],
            [{'list': 'ordered'}, {'list': 'bullet'}],

            ['link', 'image'],
            ['blockquote', 'code-block'],

            [{'size': ['small', false, 'large', 'huge']}],
            [{'header': [1, 2, 3, 4, 5, 6, false]}],

            ['clean']
        ];

        let quill = new Quill(quillEditor, {
            modules: {
                toolbar: toolbarOptions
            },
            placeholder: '请输入内容进行编辑',
            theme: 'snow'
        });

        try {
            quill.setContents(JSON.parse($(quillContent).val()));
        } catch (err) {
            try {
                quill.clipboard.dangerouslyPasteHTML($(htmlContent).val());
            } catch (err) {

            }
        }

        quill.on('text-change', function (delta, oldDelta, source) {
            $(quillContent).val(JSON.stringify(quill.getContents()));
            $(htmlContent).val(quill.root.innerHTML);
        });

        let toolbar = quill.getModule('toolbar');
        toolbar.addHandler('image', function () {
            $(quillImageFile).click();
        });

        $(quillImageFile).on('change', function () {
            if (this.files.length > 0) {
                let file = this.files[0];

                if (file.size > 2 * 1024 * 1024) {
                    return;
                }

                let formData = new FormData();
                formData.append("file", file);

                $.ajax("/api/image/upload", {
                    type: "POST",
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        let range = quill.getSelection(true);

                        quill.updateContents(new Delta()
                            .retain(range.index)
                            .delete(range.length)
                            .insert({image: response}));

                    },
                    error: function (response) {
                    }
                });
            }
        });

        return this;
    };
}(jQuery));