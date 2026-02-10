function toggleTheme() {
    document.body.classList.toggle('dark-mode');
    
    var button = document.getElementById('themeToggle');
    
    if (document.body.classList.contains('dark-mode')) {
        button.textContent = 'Light Mode';
        localStorage.setItem('theme', 'dark');
    } else {
        button.textContent = 'Dark Mode';
        localStorage.setItem('theme', 'light');
    }
}

// Charger le thème sauvegardé au démarrage
document.addEventListener('DOMContentLoaded', function() {
    var savedTheme = localStorage.getItem('theme');
    var button = document.getElementById('themeToggle');
    
    if (savedTheme === 'dark') {
        document.body.classList.add('dark-mode');
        button.textContent = 'Light Mode';
    }
});