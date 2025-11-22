(() => {
  const api = {
    async request(path, { method = 'GET', body, isForm = false } = {}) {
      const token = localStorage.getItem('token');
      const headers = {};
      if (!isForm) headers['Content-Type'] = 'application/json';
      if (token) headers['Authorization'] = `Bearer ${token}`;
      const res = await fetch(path, {
        method,
        headers,
        body: isForm ? body : body ? JSON.stringify(body) : undefined
      });
      if (!res.ok) throw await res.json().catch(() => ({ error: res.statusText }));
      const text = await res.text();
      return text ? JSON.parse(text) : null;
    },
    signin: (username, password) => api.request('/api/auth/signin', { method: 'POST', body: { username, password } }),
    signup: (data) => api.request('/api/auth/signup', { method: 'POST', body: data }),
    myIssues: () => api.request('/api/issues/my-issues'),
    stats: () => api.request('/api/issues/stats'),
    adminStats: () => api.request('/api/admin/stats'),
    adminIssuesByStatus: (status) => api.request(`/api/admin/issues/status/${status}?size=100`),
    adminGetIssue: (id) => api.request(`/api/admin/issues/${id}`),
    adminGetAllIssues: (page = 0, size = 100) => api.request(`/api/admin/issues?page=${page}&size=${size}`),
    updateIssueStatus: (id, statusData) => api.request(`/api/admin/issues/${id}/status`, { method: 'PUT', body: statusData }),
    analytics: {
      dashboard: () => api.request('/api/analytics/dashboard'),
      trends: (days) => api.request(`/api/analytics/issues/trends?days=${days}`),
      byType: () => api.request('/api/analytics/issues/by-type'),
      byStatus: () => api.request('/api/analytics/issues/by-status'),
      topLocations: (limit) => api.request(`/api/analytics/top-locations?limit=${limit}`),
      responseTimes: () => api.request('/api/analytics/response-times')
    },
    createIssue: (issue, photo) => {
      const fd = new FormData();
      fd.append('issue', new Blob([JSON.stringify(issue)], { type: 'application/json' }));
      if (photo) fd.append('photo', photo);
      return api.request('/api/issues', { method: 'POST', body: fd, isForm: true });
    },
  };

  // Routing
  const views = {
    home: document.getElementById('view-home'),
    report: document.getElementById('view-report'),
    myIssues: document.getElementById('view-my-issues'),
    admin: document.getElementById('view-admin'),
    analytics: document.getElementById('analytics-panel')
  };
  const navLinks = document.querySelectorAll('[data-nav]');
  navLinks.forEach(a => a.addEventListener('click', (e) => {
    e.preventDefault();
    const view = a.getAttribute('data-nav');
    showView(view);
  }));

  function showView(name) {
    // Hide all views
    Object.values(views).forEach(v => {
      if (v) v.classList.remove('active');
    });
    
    // Show selected view
    if (name === 'home' && views.home) { 
      views.home.classList.add('active');
    }
    if (name === 'report' && views.report) { 
      views.report.classList.add('active');
    }
    if (name === 'my-issues' && views.myIssues) { 
      views.myIssues.classList.add('active');
      loadMyIssues(); 
    }
    if (name === 'admin' && views.admin) { 
      const user = JSON.parse(localStorage.getItem('user') || 'null');
      if (!user || (user.role !== 'ADMIN' && user.role !== 'ROLE_ADMIN')) {
        alert('Admin access required. Please login with an admin account.\n\nTo create an admin account:\n1. Register a user\n2. Go to http://localhost:8080/h2-console\n3. Run: UPDATE users SET role = \'ADMIN\' WHERE username = \'your_username\';');
        showView('home');
        return;
      }
      views.admin.classList.add('active');
      loadAdmin(); 
      loadKanban(); 
    }
    if (name === 'analytics' && views.analytics) { 
      views.analytics.classList.add('active');
      loadAnalytics(); 
    }
  }

  // Auth modal
  const modal = document.getElementById('modal-auth');
  const btnLogin = document.getElementById('btn-login');
  const btnRegister = document.getElementById('btn-register');
  const btnLogout = document.getElementById('btn-logout');
  const userMenu = document.getElementById('user-menu');
  const userName = document.getElementById('user-name');
  const adminOnlyLinks = document.querySelectorAll('.admin-only');

  function updateAuthUI() {
    const stored = localStorage.getItem('user');
    const user = stored ? JSON.parse(stored) : null;
    if (user) {
      btnLogin.style.display = 'none';
      btnRegister.style.display = 'none';
      userMenu.style.display = 'flex';
      userName.textContent = user.fullName;
      // Show/hide admin links based on role
      adminOnlyLinks.forEach(link => {
        const isAdmin = user.role === 'ADMIN' || user.role === 'ROLE_ADMIN';
        link.style.display = isAdmin ? 'inline-block' : 'none';
      });
      // Debug: log user role
      console.log('User role:', user.role, 'Is Admin:', user.role === 'ADMIN' || user.role === 'ROLE_ADMIN');
    } else {
      btnLogin.style.display = 'inline-block';
      btnRegister.style.display = 'inline-block';
      userMenu.style.display = 'none';
      adminOnlyLinks.forEach(link => {
        link.style.display = 'none';
      });
    }
  }

  function openModal(tab = 'login') {
    modal.classList.add('show');
    switchTab(tab);
  }
  function closeModal() { modal.classList.remove('show'); }
  document.getElementById('modal-close').addEventListener('click', closeModal);
  btnLogin.addEventListener('click', () => openModal('login'));
  btnRegister.addEventListener('click', () => openModal('register'));
  btnLogout.addEventListener('click', () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    updateAuthUI();
    showView('home');
  });

  // Tabs
  const tabs = document.querySelectorAll('.tab');
  tabs.forEach(t => t.addEventListener('click', () => switchTab(t.dataset.tab)));
  function switchTab(name) {
    document.querySelectorAll('.tab').forEach(t => t.classList.toggle('active', t.dataset.tab === name));
    document.querySelectorAll('.tab-content').forEach(c => c.classList.toggle('active', c.id === 'tab-' + name));
  }

  // Login
  document.getElementById('form-login').addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    const msg = document.getElementById('login-message');
    msg.textContent = 'Signing in...';
    try {
      const data = await api.signin(form.username.value.trim(), form.password.value);
      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(data.user));
      msg.textContent = 'Success!';
      updateAuthUI();
      closeModal();
      showView('report');
    } catch (err) {
      msg.textContent = err.error || 'Login failed';
    }
  });

  // Register
  document.getElementById('form-register').addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    const msg = document.getElementById('register-message');
    msg.textContent = 'Creating account...';
    try {
      const payload = {
        fullName: form.fullName.value.trim(),
        username: form.username.value.trim(),
        email: form.email.value.trim(),
        password: form.password.value
      };
      const data = await api.signup(payload);
      // Some backends may not return token on signup; fallback to prompting login
      if (data.token) localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify({ ...payload, role: 'CITIZEN' }));
      msg.textContent = 'Account created. Please login.';
      switchTab('login');
    } catch (err) {
      msg.textContent = err.error || 'Registration failed';
    }
  });

  // Photo preview
  const photoInput = document.getElementById('photo');
  const photoPreview = document.getElementById('photo-preview');
  photoInput.addEventListener('change', () => {
    const file = photoInput.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = e => { photoPreview.src = e.target.result; photoPreview.style.display = 'block'; };
      reader.readAsDataURL(file);
    } else {
      photoPreview.style.display = 'none';
    }
  });

  // AI suggestion for issue type using MobileNet
  let mobilenetModel = null;
  const aiMsg = document.getElementById('ai-suggest');
  if (window.mobilenet && window.tf) {
    mobilenet.load().then(m => { mobilenetModel = m; }).catch(() => {});
  }
  photoInput.addEventListener('change', async () => {
    const file = photoInput.files[0];
    if (!file || !mobilenetModel) return;
    aiMsg.textContent = 'Analyzing photo...';
    const img = new Image();
    img.onload = async () => {
      try {
        const preds = await mobilenetModel.classify(img);
        if (preds && preds.length) {
          const label = preds[0].className.toLowerCase();
          let suggest = null;
          if (label.includes('hole') || label.includes('asphalt')) suggest = 'POTHOLE';
          else if (label.includes('lamp') || label.includes('light')) suggest = 'BROKEN_STREETLIGHT';
          else if (label.includes('trash') || label.includes('garbage') || label.includes('bin')) suggest = 'GARBAGE';
          else if (label.includes('water') || label.includes('puddle')) suggest = 'WATER_STAGNATION';
          if (suggest) {
            document.querySelector('select[name="issueType"]').value = suggest;
            aiMsg.textContent = `AI suggested: ${suggest.replace('_',' ')}`;
          } else aiMsg.textContent = 'AI could not suggest a type.';
        }
      } catch { aiMsg.textContent = 'AI analysis failed.'; }
    };
    img.src = URL.createObjectURL(file);
  });

  // Map with draggable pin and reverse geocoding
  function initMap() {
    const mapEl = document.getElementById('map');
    if (!mapEl || !window.L) return;
    const map = L.map('map').setView([20.5937, 78.9629], 5);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 19, attribution: '&copy; OpenStreetMap' }).addTo(map);
    const marker = L.marker([20.5937, 78.9629], { draggable: true }).addTo(map);
    const update = async (lat, lng) => {
      document.getElementById('lat').textContent = lat.toFixed(6);
      document.getElementById('lng').textContent = lng.toFixed(6);
      try {
        const res = await fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lng}`);
        const data = await res.json();
        const addr = data.display_name || 'Selected location';
        document.getElementById('rev-addr').textContent = addr;
        const loc = document.querySelector('input[name="location"]');
        if (loc && (!loc.value || loc.value.length < 6)) loc.value = addr;
      } catch { document.getElementById('rev-addr').textContent = 'Location selected'; }
    };
    marker.on('dragend', (e) => { const p = e.target.getLatLng(); update(p.lat,p.lng); });
    map.on('click', (e) => { marker.setLatLng(e.latlng); update(e.latlng.lat, e.latlng.lng); });
  }
  // Defer map init until after DOM paint
  setTimeout(initMap, 0);

  // Report issue
  document.getElementById('form-report').addEventListener('submit', async (e) => {
    e.preventDefault();
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    const msg = document.getElementById('report-message');
    if (!localStorage.getItem('token') || !user) { openModal('login'); return; }
    msg.textContent = 'Submitting...';
    const form = e.target;
    const issue = {
      issueType: form.issueType.value,
      description: form.description.value.trim(),
      location: form.location.value.trim()
    };
    try {
      await api.createIssue(issue, photoInput.files[0]);
      msg.textContent = 'Report submitted successfully!';
      form.reset();
      photoPreview.style.display = 'none';
      loadMyIssues();
      showView('my-issues');
    } catch (err) {
      msg.textContent = err.error || 'Submission failed';
    }
  });

  // My issues
  async function loadMyIssues() {
    const list = document.getElementById('my-issues-list');
    list.innerHTML = '<div class="issue">Loading...</div>';
    try {
      const items = await api.myIssues();
      if (!items || items.length === 0) {
        list.innerHTML = '<div class="issue">No issues yet. Report one!</div>';
        return;
      }
      list.innerHTML = items.map(renderIssueCard).join('');
      renderBadges(items);
      attachTimelineHandlers(items);
    } catch (err) {
      list.innerHTML = `<div class="issue">${err.error || 'Failed to load issues'}</div>`;
    }
  }

  function renderIssueCard(issue) {
    const date = new Date(issue.createdAt || Date.now()).toLocaleString();
    const photo = issue.photoUrl ? `<img src="/${issue.photoUrl}" alt="photo" class="thumb"/>` : '';
    return `
      <div class="issue" data-id="${issue.id}">
        <div class="flex-between">
          <strong>${issue.issueType?.replace('_',' ') || 'Issue'}</strong>
          <span class="badge ${issue.status}">${(issue.status||'PENDING').replace('_',' ')}</span>
        </div>
        <div class="muted" style="color:#9aa1ba;margin:6px 0">${date}</div>
        <div>${issue.description || ''}</div>
        <div style="margin-top:8px;color:#cfd3e7"><i class="fa-solid fa-location-dot"></i> ${issue.location || ''}</div>
        ${photo}
        <div class="actions" style="margin-top:8px;gap:8px">
          <button class="btn ghost btn-timeline" data-id="${issue.id}">View Timeline</button>
          <button class="btn ghost btn-share" data-id="${issue.id}">Share</button>
        </div>
      </div>`;
  }

  // Badges from activity
  function renderBadges(items) {
    const wrap = document.getElementById('badge-strip');
    if (!wrap) return;
    const total = items.length;
    const resolved = items.filter(i => i.status === 'RESOLVED').length;
    const badges = [];
    if (total >= 1) badges.push('Trailblazer');
    if (resolved >= 1) badges.push('Fix Finder');
    if (resolved >= 3) badges.push('Community Hero');
    if (total >= 5) badges.push('Top Contributor');
    wrap.innerHTML = badges.map(b => `<span class="badge-pill">🏅 ${b}</span>`).join('');
  }

  // Timeline modal
  const modalTimeline = document.getElementById('modal-timeline');
  const closeTl = document.querySelector('[data-close="timeline"]');
  if (closeTl) closeTl.addEventListener('click', () => modalTimeline.classList.remove('show'));
  function attachTimelineHandlers(items) {
    document.querySelectorAll('.btn-timeline').forEach(b => b.addEventListener('click', () => openTimeline(b.dataset.id)));
    document.querySelectorAll('.btn-share').forEach(b => b.addEventListener('click', () => openShare(items.find(i => i.id == b.dataset.id))));
  }
  async function openTimeline(id) {
    modalTimeline.classList.add('show');
    const cont = document.getElementById('timeline');
    cont.innerHTML = '<div class="t-item"><div class="t-dot"></div><div>Loading...</div></div>';
    try {
      const updates = await api.request(`/api/issues/${id}/updates`);
      cont.innerHTML = updates.map(u => `
        <div class="t-item">
          <div class="t-dot"></div>
          <div>
            <strong>${(u.newStatus||'').replace('_',' ')}</strong>
            <div class="muted" style="color:#9aa1ba">${new Date(u.createdAt).toLocaleString()}</div>
            <div>${u.comments||''}</div>
          </div>
        </div>`).join('');
    } catch { cont.innerHTML = '<div class="t-item"><div class="t-dot"></div><div>Failed to load</div></div>'; }
  }

  // Shareable card
  const modalShare = document.getElementById('modal-share');
  const closeShare = document.querySelector('[data-close="share"]');
  if (closeShare) closeShare.addEventListener('click', () => modalShare.classList.remove('show'));
  const btnDownload = document.getElementById('btn-download-card');
  if (btnDownload) btnDownload.addEventListener('click', () => downloadCanvas('share-canvas', 'report-card.png'));
  const btnNativeShare = document.getElementById('btn-native-share');
  if (btnNativeShare) btnNativeShare.addEventListener('click', async () => {
    const c = document.getElementById('share-canvas');
    if (navigator.share && c.toBlob) {
      c.toBlob(async blob => {
        try { await navigator.share({ files: [new File([blob], 'report.png', { type: 'image/png' })], title: 'My Community Report' }); } catch {}
      });
    }
  });
  function openShare(issue) {
    modalShare.classList.add('show');
    const c = document.getElementById('share-canvas');
    const ctx = c.getContext('2d');
    ctx.clearRect(0,0,c.width,c.height);
    ctx.fillStyle = '#0f1222'; ctx.fillRect(0,0,c.width,c.height);
    const grad = ctx.createLinearGradient(0,0,c.width,0); grad.addColorStop(0,'#6c8bff'); grad.addColorStop(1,'#69f0ae');
    ctx.fillStyle = grad; ctx.fillRect(0,0,c.width,8);
    ctx.fillStyle = '#e8e9ef';
    ctx.font = '28px Inter'; ctx.fillText('Public Service Report', 24, 48);
    ctx.font = '18px Inter'; ctx.fillText(`Type: ${(issue.issueType||'').replace('_',' ')}`, 24, 90);
    ctx.fillText(`Status: ${(issue.status||'').replace('_',' ')}`, 24, 120);
    ctx.fillText(`Location: ${issue.location||''}`, 24, 150);
    wrapText(ctx, issue.description||'', 24, 190, 740, 22);
    if (issue.photoUrl) { const img = new Image(); img.onload=()=>ctx.drawImage(img, c.width-350, 60, 320, 240); img.src='/' + issue.photoUrl; }
  }
  function wrapText(ctx, text, x, y, maxWidth, lh) {
    const words = text.split(' '); let line = '';
    for (let i=0;i<words.length;i++) {
      const test = line + words[i] + ' ';
      if (ctx.measureText(test).width > maxWidth && i>0) { ctx.fillText(line, x, y); line = words[i] + ' '; y += lh; }
      else line = test;
    }
    ctx.fillText(line, x, y);
  }

  function downloadCanvas(id, filename) {
    const a = document.createElement('a'); a.download = filename; a.href = document.getElementById(id).toDataURL(); a.click();
  }

  // Admin Kanban
  async function loadKanban() {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    if (!user || user.role !== 'ADMIN') {
      console.log('Not admin user, skipping kanban load');
      return;
    }
    try {
      const [pending, inprog, resolved, rejected] = await Promise.all([
        api.adminIssuesByStatus('PENDING'),
        api.adminIssuesByStatus('IN_PROGRESS'),
        api.adminIssuesByStatus('RESOLVED'),
        api.adminIssuesByStatus('REJECTED')
      ]);
      fillCol('col-pending', pending?.content || pending || []);
      fillCol('col-inprog', inprog?.content || inprog || []);
      fillCol('col-resolved', resolved?.content || resolved || []);
      fillCol('col-rejected', rejected?.content || rejected || []);
      enableDnD();
    } catch (err) {
      console.error('Error loading kanban:', err);
      // Show error message in kanban columns
      ['col-pending', 'col-inprog', 'col-resolved', 'col-rejected'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.innerHTML = '<div class="issue">Failed to load issues</div>';
      });
    }
  }
  function fillCol(id, items) {
    const el = document.getElementById(id); if (!el) return;
    if (items.length === 0) {
      el.innerHTML = '<div class="issue" style="text-align:center;color:var(--muted);padding:20px">No issues</div>';
      return;
    }
    el.innerHTML = items.map(i => {
      const date = new Date(i.createdAt || Date.now()).toLocaleDateString();
      const desc = (i.description || '').substring(0, 80) + ((i.description || '').length > 80 ? '...' : '');
      const photo = i.photoUrl ? `<img src="/${i.photoUrl}" alt="photo" class="thumb" style="max-height:100px;margin-top:8px"/>` : '';
      const reporter = i.reportedBy ? `<div style="font-size:11px;color:var(--muted);margin-top:4px"><i class="fa-solid fa-user"></i> ${i.reportedBy.fullName || i.reportedBy.username || 'Unknown'}</div>` : '';
      return `
        <div class="issue card-draggable" draggable="true" data-id="${i.id}" style="cursor:pointer">
          <div class="flex-between" style="margin-bottom:6px">
            <strong style="font-size:14px">${(i.issueType || '').replace('_',' ')}</strong>
            <span class="badge ${i.status}">${(i.status||'PENDING').replace('_',' ')}</span>
          </div>
          <div style="font-size:12px;color:var(--muted);margin-bottom:6px">${date}</div>
          <div style="font-size:13px;margin-bottom:6px">${desc}</div>
          <div style="font-size:11px;color:var(--muted)"><i class="fa-solid fa-location-dot"></i> ${(i.location || '').substring(0, 40)}${(i.location || '').length > 40 ? '...' : ''}</div>
          ${reporter}
          ${photo}
          <button class="btn ghost" style="width:100%;margin-top:8px;font-size:12px;padding:6px" onclick="event.stopPropagation();openIssueDetail(${i.id})">View Details</button>
        </div>`;
    }).join('');
    // Add click handlers to cards
    el.querySelectorAll('.card-draggable').forEach(card => {
      card.addEventListener('click', (e) => {
        if (!e.target.closest('button')) {
          const id = card.dataset.id;
          openIssueDetail(id);
        }
      });
    });
  }
  function enableDnD() {
    const drags = document.querySelectorAll('.card-draggable');
    const cols = document.querySelectorAll('.col-body');
    drags.forEach(d => {
      d.addEventListener('dragstart', () => d.classList.add('dragging'));
      d.addEventListener('dragend', () => d.classList.remove('dragging'));
    });
    cols.forEach(col => {
      col.addEventListener('dragover', e => { e.preventDefault(); const cur = document.querySelector('.dragging'); if (cur) col.appendChild(cur); });
      col.addEventListener('drop', async (e) => {
        e.preventDefault();
        const cur = document.querySelector('.dragging'); 
        if (!cur) return;
        const id = cur.dataset.id; 
        const status = col.parentElement.getAttribute('data-status');
        const comments = prompt(`Update status to ${status.replace('_',' ')}. Add comments (optional):`);
        if (comments === null) {
          // User cancelled, reload to reset position
          loadKanban();
          return;
        }
        try { 
          await api.updateIssueStatus(id, { status, comments: comments || '' });
          // Reload kanban after update
          loadKanban();
        } catch (err) {
          console.error('Error updating issue status:', err);
          alert('Failed to update issue status: ' + (err.error || err.message || 'Unknown error'));
          loadKanban(); // Reload on error too
        }
      });
    });
  }

  // Admin
  async function loadAdmin() {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    if (!user || user.role !== 'ADMIN') {
      console.log('Not admin user, skipping admin load');
      return;
    }
    const statsEl = document.getElementById('admin-stats');
    if (!statsEl) {
      console.error('Admin stats element not found');
      return;
    }
    try {
      const s = await api.adminStats();
      statsEl.innerHTML = `
        <div class="stat">
          <div style="font-size:12px;color:var(--muted);margin-bottom:4px">Pending</div>
          <strong style="font-size:24px;color:#ffd166">${s.pending||0}</strong>
        </div>
        <div class="stat">
          <div style="font-size:12px;color:var(--muted);margin-bottom:4px">In Progress</div>
          <strong style="font-size:24px;color:#6c8bff">${s.inProgress||0}</strong>
        </div>
        <div class="stat">
          <div style="font-size:12px;color:var(--muted);margin-bottom:4px">Resolved</div>
          <strong style="font-size:24px;color:#69f0ae">${s.resolved||0}</strong>
        </div>
        <div class="stat">
          <div style="font-size:12px;color:var(--muted);margin-bottom:4px">Rejected</div>
          <strong style="font-size:24px;color:#ff6b6b">${s.rejected||0}</strong>
        </div>`;
    } catch (err) {
      console.error('Error loading admin stats:', err);
      statsEl.textContent = 'Failed to load stats: ' + (err.error || err.message || 'Unknown error');
    }
  }

  // Issue Detail Modal
  const modalIssueDetail = document.getElementById('modal-issue-detail');
  const closeIssueDetail = document.querySelector('[data-close="issue-detail"]');
  if (closeIssueDetail) closeIssueDetail.addEventListener('click', () => modalIssueDetail.classList.remove('show'));
  
  async function openIssueDetail(id) {
    modalIssueDetail.classList.add('show');
    const content = document.getElementById('issue-detail-content');
    content.innerHTML = '<div style="text-align:center;padding:20px">Loading...</div>';
    
    try {
      const issue = await api.adminGetIssue(id);
      const date = new Date(issue.createdAt || Date.now()).toLocaleString();
      const updatedDate = issue.updatedAt ? new Date(issue.updatedAt).toLocaleString() : 'N/A';
      const resolvedDate = issue.resolvedAt ? new Date(issue.resolvedAt).toLocaleString() : 'N/A';
      const photo = issue.photoUrl ? `<img src="/${issue.photoUrl}" alt="photo" style="width:100%;max-height:300px;object-fit:cover;border-radius:10px;margin-top:8px"/>` : '<div style="text-align:center;padding:20px;color:var(--muted)">No photo</div>';
      
      content.innerHTML = `
        <div class="stack" style="gap:16px">
          <div>
            <div class="flex-between" style="margin-bottom:12px">
              <h3 style="margin:0">${(issue.issueType || '').replace('_',' ')}</h3>
              <span class="badge ${issue.status}">${(issue.status||'PENDING').replace('_',' ')}</span>
            </div>
            <div style="font-size:13px;color:var(--muted);margin-bottom:8px">
              <div>Reported: ${date}</div>
              <div>Updated: ${updatedDate}</div>
              ${issue.resolvedAt ? `<div>Resolved: ${resolvedDate}</div>` : ''}
            </div>
          </div>
          
          <div>
            <strong style="font-size:12px;color:var(--muted);text-transform:uppercase">Description</strong>
            <div style="margin-top:6px;padding:12px;background:rgba(255,255,255,.03);border-radius:8px">${issue.description || 'N/A'}</div>
          </div>
          
          <div>
            <strong style="font-size:12px;color:var(--muted);text-transform:uppercase">Location</strong>
            <div style="margin-top:6px;padding:12px;background:rgba(255,255,255,.03);border-radius:8px">
              <i class="fa-solid fa-location-dot"></i> ${issue.location || 'N/A'}
            </div>
          </div>
          
          <div>
            <strong style="font-size:12px;color:var(--muted);text-transform:uppercase">Reporter</strong>
            <div style="margin-top:6px;padding:12px;background:rgba(255,255,255,.03);border-radius:8px">
              ${issue.reportedBy ? `${issue.reportedBy.fullName || issue.reportedBy.username || 'Unknown'} (${issue.reportedBy.email || 'N/A'})` : 'N/A'}
            </div>
          </div>
          
          ${issue.assignedTo ? `
          <div>
            <strong style="font-size:12px;color:var(--muted);text-transform:uppercase">Assigned To</strong>
            <div style="margin-top:6px;padding:12px;background:rgba(255,255,255,.03);border-radius:8px">
              ${issue.assignedTo.fullName || issue.assignedTo.username || 'Unknown'}
            </div>
          </div>
          ` : ''}
          
          ${issue.adminComments ? `
          <div>
            <strong style="font-size:12px;color:var(--muted);text-transform:uppercase">Admin Comments</strong>
            <div style="margin-top:6px;padding:12px;background:rgba(255,255,255,.03);border-radius:8px">${issue.adminComments}</div>
          </div>
          ` : ''}
          
          <div>
            <strong style="font-size:12px;color:var(--muted);text-transform:uppercase">Photo</strong>
            ${photo}
          </div>
          
          <div style="border-top:1px solid rgba(255,255,255,.1);padding-top:16px;margin-top:8px">
            <strong style="font-size:12px;color:var(--muted);text-transform:uppercase;margin-bottom:8px;display:block">Update Status</strong>
            <form id="form-update-status" class="stack" style="gap:10px">
              <select id="update-status-select" required style="background:#0e1326;color:#fff;border:1px solid rgba(255,255,255,.08);border-radius:10px;padding:10px">
                <option value="PENDING" ${issue.status === 'PENDING' ? 'selected' : ''}>Pending</option>
                <option value="IN_PROGRESS" ${issue.status === 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                <option value="RESOLVED" ${issue.status === 'RESOLVED' ? 'selected' : ''}>Resolved</option>
                <option value="REJECTED" ${issue.status === 'REJECTED' ? 'selected' : ''}>Rejected</option>
              </select>
              <textarea id="update-status-comments" placeholder="Add comments (optional)" rows="3" style="background:#0e1326;color:#fff;border:1px solid rgba(255,255,255,.08);border-radius:10px;padding:10px;resize:vertical">${issue.adminComments || ''}</textarea>
              <button type="submit" class="btn">Update Status</button>
              <span id="update-status-message" class="message"></span>
            </form>
          </div>
        </div>
      `;
      
      // Handle form submission
      const form = document.getElementById('form-update-status');
      if (form) {
        form.onsubmit = null; // Remove old handlers
        form.addEventListener('submit', async (e) => {
          e.preventDefault();
          const msg = document.getElementById('update-status-message');
          msg.textContent = 'Updating...';
          try {
            await api.updateIssueStatus(id, {
              status: document.getElementById('update-status-select').value,
              comments: document.getElementById('update-status-comments').value || ''
            });
            msg.textContent = 'Status updated successfully!';
            setTimeout(() => {
              modalIssueDetail.classList.remove('show');
              loadKanban();
              loadAdmin();
            }, 1000);
          } catch (err) {
            msg.textContent = err.error || 'Failed to update status';
          }
        });
      }
    } catch (err) {
      content.innerHTML = `<div style="text-align:center;padding:20px;color:var(--danger)">Failed to load issue: ${err.error || err.message || 'Unknown error'}</div>`;
    }
  }
  
  // Make openIssueDetail available globally
  window.openIssueDetail = openIssueDetail;

  // Analytics
  let typeChart, statusChart, trendsChart;
  async function loadAnalytics() {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    if (!user || user.role !== 'ADMIN') {
      console.log('Not admin user, skipping analytics load');
      return;
    }
    try {
      // Load overview stats
      const dashboard = await api.analytics.dashboard();
      const overviewEl = document.getElementById('overview-stats');
      if (overviewEl) {
        overviewEl.innerHTML = `
          <div class="stat-card">
            <div class="stat-value">${dashboard.totalIssues || 0}</div>
            <div class="stat-label">Total Issues</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">${dashboard.resolvedIssues || 0}</div>
            <div class="stat-label">Resolved</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">${dashboard.pendingIssues || 0}</div>
            <div class="stat-label">Pending</div>
          </div>
          <div class="stat-card">
            <div class="stat-value">${dashboard.avgResolutionTime || 0}</div>
            <div class="stat-label">Avg Resolution (days)</div>
          </div>
        `;
      }

      // Load issues by type
      const byType = await api.analytics.byType();
      const typeCtx = document.getElementById('type-chart')?.getContext('2d');
      if (typeCtx && byType) {
        if (typeChart) typeChart.destroy();
        typeChart = new Chart(typeCtx, {
          type: 'doughnut',
          data: {
            labels: Object.keys(byType),
            datasets: [{ data: Object.values(byType), backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0'] }]
          }
        });
      }

      // Load issues by status
      const byStatus = await api.analytics.byStatus();
      const statusCtx = document.getElementById('status-chart')?.getContext('2d');
      if (statusCtx && byStatus) {
        if (statusChart) statusChart.destroy();
        statusChart = new Chart(statusCtx, {
          type: 'bar',
          data: {
            labels: Object.keys(byStatus),
            datasets: [{ label: 'Issues', data: Object.values(byStatus), backgroundColor: '#36A2EB' }]
          }
        });
      }

      // Load trends
      const trends = await api.analytics.trends(30);
      const trendsCtx = document.getElementById('trends-chart')?.getContext('2d');
      if (trendsCtx && trends) {
        if (trendsChart) trendsChart.destroy();
        trendsChart = new Chart(trendsCtx, {
          type: 'line',
          data: {
            labels: trends.dates || [],
            datasets: [{ label: 'Issues Reported', data: trends.counts || [], borderColor: '#36A2EB', fill: false }]
          }
        });
      }

      // Load top locations
      const topLoc = await api.analytics.topLocations(10);
      const topLocEl = document.getElementById('top-locations');
      if (topLocEl && topLoc) {
        topLocEl.innerHTML = topLoc.map(l => 
          `<div class="location-item"><strong>${l.location}</strong><span>${l.count} issues</span></div>`
        ).join('');
      }

      // Load response times
      const respTimes = await api.analytics.responseTimes();
      const respEl = document.getElementById('response-times');
      if (respEl && respTimes) {
        respEl.innerHTML = `
          <div class="stat-item">Average: <strong>${respTimes.average || 0} days</strong></div>
          <div class="stat-item">Fastest: <strong>${respTimes.fastest || 0} days</strong></div>
          <div class="stat-item">Slowest: <strong>${respTimes.slowest || 0} days</strong></div>
        `;
      }
    } catch (err) {
      console.error('Error loading analytics:', err);
      const panel = document.getElementById('analytics-panel');
      if (panel) {
        panel.innerHTML = '<div class="error">Failed to load analytics: ' + (err.error || err.message || 'Unknown error') + '</div>';
      }
    }
  }

  // Handle hash navigation (for direct admin access)
  window.addEventListener('hashchange', () => {
    const hash = window.location.hash.substring(1);
    if (hash && views[hash]) {
      showView(hash);
    }
  });

  // Check hash on load
  if (window.location.hash) {
    const hash = window.location.hash.substring(1);
    if (hash && views[hash]) {
      showView(hash);
    }
  }

  // Init
  // Theme toggle (persist)
  const root = document.documentElement;
  const savedTheme = localStorage.getItem('theme');
  if (savedTheme === 'light') root.classList.add('light');
  const addThemeButton = () => {
    const host = document.querySelector('.auth-actions');
    if (!host || host.querySelector('#theme-toggle')) return;
    const btn = document.createElement('button');
    btn.id = 'theme-toggle';
    btn.className = 'btn ghost';
    btn.textContent = savedTheme === 'light' ? 'Dark' : 'Light';
    btn.onclick = () => {
      root.classList.toggle('light');
      const mode = root.classList.contains('light') ? 'light' : 'dark';
      localStorage.setItem('theme', mode);
      btn.textContent = mode === 'light' ? 'Dark' : 'Light';
    };
    host.prepend(btn);
  };

  updateAuthUI();
  addThemeButton();
  
  // Show view based on hash or default to home
  const hash = window.location.hash.substring(1);
  if (hash && views[hash]) {
    showView(hash);
  } else {
    showView('home');
  }
})();



