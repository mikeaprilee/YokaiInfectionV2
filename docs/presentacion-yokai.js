document.addEventListener("DOMContentLoaded", () => {
  const sections = [...document.querySelectorAll("main section[id]")];
  const links = [...document.querySelectorAll("nav a[href^='#']")];

  const updateActiveLink = () => {
    const currentSection = sections.reduce((active, section) => {
      return section.getBoundingClientRect().top <= 120 ? section : active;
    }, sections[0]);

    links.forEach((link) => {
      link.classList.toggle(
        "active",
        link.getAttribute("href") === `#${currentSection.id}`
      );
    });
  };

  updateActiveLink();
  window.addEventListener("scroll", updateActiveLink, { passive: true });
});
