document.addEventListener("DOMContentLoaded", function() {
    // var role = localStorage.getItem('jwt'); // Nếu chưa đăng nhập, coi như khách (GUEST)
    var role = localStorage.getItem('role'); // Nếu chưa đăng nhập, coi như khách (GUEST)
    if(!role || role == "null") role = "GUEST";
    var menu = document.getElementById('menu'); // Lấy thẻ <ul> menu
    var menuItems = [
        { text: "Trang Chủ", link: "/api/public/", showFor: ["USER", "GUEST"] },
        { text: "Đăng Ký", link: "/api/public/register", showFor: ["GUEST"] },
        { text: "Đăng Nhập", link: "/api/public/login", showFor: ["GUEST"] },
        { text: "Tài Khoản", link: "/api/user/profile", showFor: ["USER"] },
        { text: "Đăng Xuất", link: "javascript:logout()", showFor: ["USER"] }
    ];

    menuItems.forEach(item => {
        if (item.showFor.includes(role)) {
            var li = document.createElement("li");
            var a = document.createElement("a");
            a.href = item.link;
            a.textContent = item.text;
            li.appendChild(a);
            if (item.text === "Đăng Xuất") {
                li.addEventListener("click", logout);
                a.removeAttribute("href"); // Xóa href để tránh chuyển trang
            }

            menu.appendChild(li);
        }
    });
});
