(function () {
  "use strict";
  var figure = document.querySelector(".dog-figure");
  if (!figure) return;
  figure.addEventListener("touchstart", function () {
    figure.classList.toggle("is-hover");
  }, { passive: true });
})();
