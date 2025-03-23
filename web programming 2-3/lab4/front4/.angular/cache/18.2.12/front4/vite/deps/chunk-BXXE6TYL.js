import {
  BaseStyle,
  PrimeNG,
  PrimeTemplate,
  base,
  config_default,
  getKeyValue,
  service_default,
  uuid
} from "./chunk-MZVPSIJ2.js";
import {
  DOCUMENT,
  isPlatformServer
} from "./chunk-IBXZIRMC.js";
import {
  ChangeDetectorRef,
  ContentChildren,
  Directive,
  ElementRef,
  Injectable,
  Injector,
  Input,
  PLATFORM_ID,
  Renderer2,
  inject,
  setClassMetadata,
  ɵɵNgOnChangesFeature,
  ɵɵProvidersFeature,
  ɵɵcontentQuery,
  ɵɵdefineDirective,
  ɵɵdefineInjectable,
  ɵɵgetInheritedFactory,
  ɵɵloadQuery,
  ɵɵqueryRefresh
} from "./chunk-RTJCVXYN.js";
import {
  __spreadValues
} from "./chunk-WDMUDEB6.js";

// node_modules/primeng/fesm2022/primeng-basecomponent.mjs
var BaseComponentStyle = class _BaseComponentStyle extends BaseStyle {
  name = "common";
  static ɵfac = /* @__PURE__ */ (() => {
    let ɵBaseComponentStyle_BaseFactory;
    return function BaseComponentStyle_Factory(__ngFactoryType__) {
      return (ɵBaseComponentStyle_BaseFactory || (ɵBaseComponentStyle_BaseFactory = ɵɵgetInheritedFactory(_BaseComponentStyle)))(__ngFactoryType__ || _BaseComponentStyle);
    };
  })();
  static ɵprov = ɵɵdefineInjectable({
    token: _BaseComponentStyle,
    factory: _BaseComponentStyle.ɵfac,
    providedIn: "root"
  });
};
(() => {
  (typeof ngDevMode === "undefined" || ngDevMode) && setClassMetadata(BaseComponentStyle, [{
    type: Injectable,
    args: [{
      providedIn: "root"
    }]
  }], null, null);
})();
var BaseComponent = class _BaseComponent {
  document = inject(DOCUMENT);
  platformId = inject(PLATFORM_ID);
  el = inject(ElementRef);
  injector = inject(Injector);
  cd = inject(ChangeDetectorRef);
  renderer = inject(Renderer2);
  config = inject(PrimeNG);
  baseComponentStyle = inject(BaseComponentStyle);
  baseStyle = inject(BaseStyle);
  scopedStyleEl;
  rootEl;
  dt;
  get styleOptions() {
    return {
      nonce: this.config?.csp().nonce
    };
  }
  get _name() {
    return this.constructor.name.replace(/^_/, "").toLowerCase();
  }
  get componentStyle() {
    return this["_componentStyle"];
  }
  attrSelector = uuid("pc");
  _getHostInstance(instance) {
    if (instance) {
      return instance ? this["hostName"] ? instance["name"] === this["hostName"] ? instance : this._getHostInstance(instance.parentInstance) : instance.parentInstance : void 0;
    }
  }
  _getOptionValue(options, key = "", params = {}) {
    return getKeyValue(options, key, params);
  }
  ngOnInit() {
    if (this.document) {
      this._loadStyles();
    }
  }
  ngAfterViewInit() {
    this.rootEl = this.el?.nativeElement;
    if (this.rootEl) {
      this.rootEl?.setAttribute(this.attrSelector, "");
    }
  }
  templates;
  ngAfterContentInit() {
    this.templates?.forEach((item) => {
      const type = item.getType();
      const template = `${type}Template`;
      if (this.hasOwnProperty(template)) {
        this[template] = item.template;
      }
      if (this.hasOwnProperty(`_${template}`)) {
        this[`_${template}`] = item.template;
      }
      this[type] = item.template;
    });
  }
  ngOnChanges(changes) {
    if (this.document && !isPlatformServer(this.platformId)) {
      const {
        dt
      } = changes;
      if (dt && dt.currentValue) {
        this._loadScopedThemeStyles(dt.currentValue);
        this._themeChangeListener(() => this._loadScopedThemeStyles(dt.currentValue));
      }
    }
  }
  ngOnDestroy() {
    this._unloadScopedThemeStyles();
  }
  _loadStyles() {
    const _load = () => {
      if (!base.isStyleNameLoaded("base")) {
        this.baseStyle.loadCSS(this.styleOptions);
        base.setLoadedStyleName("base");
      }
      this._loadThemeStyles();
    };
    _load();
    this._themeChangeListener(() => _load());
  }
  _loadCoreStyles() {
    if (!base.isStyleNameLoaded("base") && this._name) {
      this.baseComponentStyle.loadCSS(this.styleOptions);
      this.componentStyle && this.componentStyle?.loadCSS(this.styleOptions);
      base.setLoadedStyleName(this.componentStyle?.name);
    }
  }
  _loadThemeStyles() {
    if (!config_default.isStyleNameLoaded("common")) {
      const {
        primitive,
        semantic,
        global,
        style
      } = this.componentStyle?.getCommonTheme?.() || {};
      this.baseStyle.load(primitive?.css, __spreadValues({
        name: "primitive-variables"
      }, this.styleOptions));
      this.baseStyle.load(semantic?.css, __spreadValues({
        name: "semantic-variables"
      }, this.styleOptions));
      this.baseStyle.load(global?.css, __spreadValues({
        name: "global-variables"
      }, this.styleOptions));
      this.baseStyle.loadTheme(__spreadValues({
        name: "global-style"
      }, this.styleOptions), style);
      config_default.setLoadedStyleName("common");
    }
    if (!config_default.isStyleNameLoaded(this.componentStyle?.name) && this.componentStyle?.name) {
      const {
        css,
        style
      } = this.componentStyle?.getComponentTheme?.() || {};
      this.componentStyle?.load(css, __spreadValues({
        name: `${this.componentStyle?.name}-variables`
      }, this.styleOptions));
      this.componentStyle?.loadTheme(__spreadValues({
        name: `${this.componentStyle?.name}-style`
      }, this.styleOptions), style);
      config_default.setLoadedStyleName(this.componentStyle?.name);
    }
    if (!config_default.isStyleNameLoaded("layer-order")) {
      const layerOrder = this.componentStyle?.getLayerOrderThemeCSS?.();
      this.baseStyle.load(layerOrder, __spreadValues({
        name: "layer-order",
        first: true
      }, this.styleOptions));
      config_default.setLoadedStyleName("layer-order");
    }
    if (this.dt) {
      this._loadScopedThemeStyles(this.dt);
      this._themeChangeListener(() => this._loadScopedThemeStyles(this.dt));
    }
  }
  _loadScopedThemeStyles(preset) {
    const {
      css
    } = this.componentStyle?.getPresetTheme?.(preset, `[${this.attrSelector}]`) || {};
    const scopedStyle = this.componentStyle?.load(css, __spreadValues({
      name: `${this.attrSelector}-${this.componentStyle?.name}`
    }, this.styleOptions));
    this.scopedStyleEl = scopedStyle?.el;
  }
  _unloadScopedThemeStyles() {
    this.scopedStyleEl?.remove();
  }
  _themeChangeListener(callback = () => {
  }) {
    base.clearLoadedStyleNames();
    service_default.on("theme:change", callback);
  }
  cx(arg, rest) {
    const classes = this.parent ? this.parent.componentStyle?.classes?.[arg] : this.componentStyle?.classes?.[arg];
    if (typeof classes === "function") {
      return classes({
        instance: this
      });
    }
    return typeof classes === "string" ? classes : arg;
  }
  sx(arg) {
    const styles = this.componentStyle?.inlineStyles?.[arg];
    if (typeof styles === "function") {
      return styles({
        instance: this
      });
    }
    if (typeof styles === "string") {
      return styles;
    } else {
      return __spreadValues({}, styles);
    }
  }
  // cx(key = '', params = {}) {
  //     const classes = this.parent ? this.parent.componentStyle?.classes : this.componentStyle?.classes;
  //     return this._getOptionValue(classes({ instance: this._getHostInstance(this) }), key, { ...params });
  // }
  get parent() {
    return this["parentInstance"];
  }
  static ɵfac = function BaseComponent_Factory(__ngFactoryType__) {
    return new (__ngFactoryType__ || _BaseComponent)();
  };
  static ɵdir = ɵɵdefineDirective({
    type: _BaseComponent,
    contentQueries: function BaseComponent_ContentQueries(rf, ctx, dirIndex) {
      if (rf & 1) {
        ɵɵcontentQuery(dirIndex, PrimeTemplate, 4);
      }
      if (rf & 2) {
        let _t;
        ɵɵqueryRefresh(_t = ɵɵloadQuery()) && (ctx.templates = _t);
      }
    },
    inputs: {
      dt: "dt"
    },
    standalone: true,
    features: [ɵɵProvidersFeature([BaseComponentStyle, BaseStyle]), ɵɵNgOnChangesFeature]
  });
};
(() => {
  (typeof ngDevMode === "undefined" || ngDevMode) && setClassMetadata(BaseComponent, [{
    type: Directive,
    args: [{
      standalone: true,
      providers: [BaseComponentStyle, BaseStyle]
    }]
  }], null, {
    dt: [{
      type: Input
    }],
    templates: [{
      type: ContentChildren,
      args: [PrimeTemplate]
    }]
  });
})();

export {
  BaseComponent
};
//# sourceMappingURL=chunk-BXXE6TYL.js.map
