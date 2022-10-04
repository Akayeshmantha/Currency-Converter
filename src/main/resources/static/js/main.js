function LoadCurrency () {

    this.source = null;

    this.start = function () {

        const firstDropDown = document.getElementById("inlineFormCustomSelect");
        const secondDropDown = document.getElementById("inlineFormCustomSelect2");

        this.source = new EventSource("/feed/V1/symbols");

        this.source.addEventListener("message", function (event) {
            // These events are JSON, so parsing and DOM fiddling are needed
            let symbol = JSON.parse(event.data);
            let html = ""
            html += `
            <option value="${symbol.key}">${symbol.keyValue}</option>`
            firstDropDown.innerHTML += html
            secondDropDown.innerHTML += html
        });

        this.source.onerror = function () {
            this.close();
        };

    };

    this.stop = function() {
        this.source.close();
    }

}

currency = new LoadCurrency();

/*
 * Register callbacks for starting and stopping the SSE controller.
 */
window.onload = function() {
    currency.start();
};
window.onbeforeunload = function() {
    currency.stop();
}