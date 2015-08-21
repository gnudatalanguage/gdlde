package gdlde;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.*;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GDLCodeScanner extends RuleBasedScanner
{
    private static final String[] GDL_KEYWORDS = new String[] {
            "and", "begin", "break", "case", "common", "compile_opt", "continue", "do", "else",
            "end", "endcase", "endelse", "endfor", "endforeach", "endif", "endrep", "endswitch",
            "endwhile", "eq", "for", "foreach", "forward_function", "function", "ge", "goto",
            "gt", "if", "inherits", "le", "lt", "mod", "ne", "not", "of", "on_ioerror", "or",
            "pro", "repeat", "return", "switch", "then", "until", "while", "xor"
    };

    // from libinit*.cpp
    private static ArrayList<String> GDL_SYSTEM_PRODEDURES = new ArrayList<String> (Arrays.asList(
            "cpu", "svdc", "spawn", "byteorder", "message", "cd", "file_mkdir", "retall", "catch",
            "on_error", "exit", "help", "pref_set", "print", "printf", "stop", "read", "readf", "reads",
            "defsysv", "heap_gc", "heap_free", "ptr_free", "obj_destroy", "call_procedure", "call_method",
            "openr", "openw", "openu", "get_lun", "socket", "flush", "close", "free_lun", "writeu",
            "readu", "resolve_routine", "replicate_inplace", "strput", "python", "window", "wdelete",
            "wset", "wshow", "cursor", "set_plot", "tvlct", "tvcrs", "empty", "device", "usersym", "plot",
            "plot_io", "plot_oo", "plot_oi", "axis", "oplot", "plots", "set_shading", "shade_surf",
            "surface", "contour", "xyouts", "polyfill", "scale3", "t3d", "erase", "caldat", "pm",
            "gribapi_close_file", "gribapi_release", "gribapi_get", "gribapi_get_data", "sem_delete",
            "sem_release", "ludc", "journal", "ncdf_close", "ncdf_varget1", "ncdf_varget", "ncdf_diminq",
            "ncdf_attget", "ncdf_control", "ncdf_attput", "ncdf_attdel", "ncdf_attrename",
            "ncdf_dimrename", "ncdf_varrename", "ncdf_varput", "cdf_epoch", "magick_close",
            "magick_readcolormaprgb", "magick_write", "magick_writefile", "magick_quality",
            "magick_quantize", "magick_writeindexes", "magick_writecolortable", "magick_flip",
            "magick_matte", "magick_interlace", "magick_addnoise", "magick_display", "choldc",
            "la_choldc", "la_trired", "map_proj_gctp_forinit", "map_proj_gctp_revinit", "map_continents",
            "triangulate", "qhull", "grid_input", "point_lun", "linkimage", "wait", "hdf_vg_getinfo",
            "hdf_vd_get", "hdf_vg_gettrs", "hdf_vg_detach", "hdf_vd_detach", "hdf_sd_getdata",
            "hdf_sd_adddata", "hdf_sd_fileinfo", "hdf_sd_getinfo", "hdf_sd_attrinfo", "hdf_sd_endaccess",
            "hdf_sd_end", "hdf_close", "hdf_sd_dimget", "tv", "loadct_internalgdl",
            "widget_displaycontextmenu", "widget_control", "setenv", "struct_assign", "wait",
            "mpidl_send", "mpidl_finalize", "h5f_close", "h5d_close", "h5s_close", "h5a_close",
            "h5t_close", "h5g_close"
    ));

    // from libinit*.cpp
    private static ArrayList<String> GDL_SYSTEM_FUNCTIONS = new ArrayList<String> (Arrays.asList(
            "list", "hash", "scope_level", "scope_varfetch", "scope_traceback", "get_kbrd", "temporary",
            "routine_info", "bytscl", "n_tags", "obj_class", "obj_isa", "rebin", "convol", "file_search",
            "file_expand_path", "file_readlink", "expand_path", "strjoin", "strcmp", "eof", "arg_present",
            "file_test", "file_basename", "file_dirname", "file_same", "file_info", "shift", "sort",
            "median", "transpose", "recall_commands", "memory", "string", "bytarr", "intarr", "uintarr",
            "lonarr", "ulonarr", "lon64arr", "ulon64arr", "fltarr", "dblarr", "strarr", "complexarr",
            "dcomplexarr", "ptrarr", "objarr", "ptr_new", "ptr_valid", "obj_valid", "obj_new",
            "call_function", "call_method", "bindgen", "indgen", "uindgen", "sindgen", "lindgen",
            "ulindgen", "l64indgen", "ul64indgen", "findgen", "dindgen", "cindgen", "dcindgen",
            "n_elements", "execute", "assoc", "byte", "fix", "uint", "long", "ulong", "long64", "ulong64",
            "float", "double", "complex", "dcomplex", "fix", "uint", "long", "ulong", "long64", "ulong64",
            "float", "double", "complex", "dcomplex", "logical_and", "logical_or", "logical_true",
            "replicate", "sin", "cos", "tan", "sinh", "cosh", "tanh", "asin", "acos", "atan", "alog",
            "alog10", "alog", "alog10", "sqrt", "abs", "exp", "round", "ceil", "floor", "conj",
            "imaginary", "strcompress", "strlowcase", "strupcase", "strlen", "strmid", "strtrim",
            "strpos", "where", "total", "product", "n_params", "keyword_set", "array_equal", "min", "max",
            "create_struct", "rotate", "reverse", "min", "max", "python", "get_screen_size",
            "get_screen_size", "laguerre", "gauss_pdf", "gauss_cvf", "t_pdf", "julday", "newton",
            "broyden", "parse_url", "locale_get", "get_login_info", "idl_base64", "ll_arc_distance",
            "command_line_args", "imsl_constant", "get_drive_list", "imsl_binomialcoef",
            "gribapi_open_file", "gribapi_count_in_file", "gribapi_new_from_file",
            "gribapi_get_message_size", "gribapi_clone", "gribapi_get_size", "crossp", "hanning",
            "wtn", "imsl_zeropoly", "spher_harm", "gdl_erfinv", "sem_create", "sem_lock", "beseli",
            "beselj", "beselk", "besely", "lusol", "determ", "trisol", "qsimp", "qromb", "qromo",
            "fz_roots", "fx_root", "spl_init", "spl_interp", "roberts", "sobel", "prewitt",
            "matrix_multiply", "smooth2", "smooth3", "systime", "legendre", "gsl_exp", "ncdf_exists",
            "magick_exists", "proj4_exists", "proj4new_exists", "gshhg_exists", "ncdf_open",
            "ncdf_create", "ncdf_groupsinq", "ncdf_groupname", "ncdf_fullgroupname", "ncdf_groupparent",
            "ncdf_groupdef", "ncdf_dimidsinq", "ncdf_ncidinq", "ncdf_varidsinq", "ncdf_unlimdimsinq",
            "ncdf_inquire", "ncdf_varinq", "ncdf_varid", "ncdf_dimid", "ncdf_attname", "ncdf_attinq",
            "ncdf_attcopy", "ncdf_dimdef", "ncdf_vardef", "magick_open", "magick_create", "magick_read",
            "magick_readindexes", "magick_indexedcolor", "magick_rows", "magick_columns",
            "magick_colormapsize", "magick_magick", "magick_ping", "erf", "errorf", "erfc",
            "gamma", "nr_gamma", "lngamma", "igamma", "idl_igamma", "beta", "expint", "gaussint",
            "size", "fstat", "routine_names", "isa", "typename", "cholsol", "la_cholsol", "invert",
            "fft", "fft", "randomu", "randomn", "check_math", "histogram", "interpolate", "machar",
            "rk4jmg", "convert_coord", "finite", "radon", "sph_scat", "qgrid3", "trigrid", "poly_2d",
            "make_array", "reform", "hdf_ishdf", "hdf_open", "hdf_vg_getid", "hdf_vg_attach",
            "hdf_vd_attach", "hdf_vd_find", "hdf_vd_read", "hdf_sd_create", "hdf_sd_start",
            "hdf_sd_nametoindex", "hdf_sd_attrfind", "hdf_sd_select", "hdf_sd_dimgetid", "tvrd",
            "call_external", "widget_base", "widget_button", "widget_combobox", "widget_draw",
            "widget_droplist", "widget_event", "widget_info", "widget_label", "widget_list",
            "widget_propertysheet", "widget_slider", "widget_tab", "widget_table", "widget_text",
            "widget_tree", "cw_bgroup", "wxwidgets_exists", "dialog_pickfile_wxwidgets",
            "dialog_message_wxwidgets", "strtok", "getenv", "tag_names", "stregex", "clock",
            "mpidl_recv", "mpidl_allreduce", "mpidl_comm_rank", "mpidl_comm_size", "h5f_open",
            "h5d_open", "h5d_read", "h5d_get_space", "h5f_is_hdf5", "h5_get_libversion", "h5d_get_type",
            "h5t_get_size", "h5a_open_name", "h5a_open_idx", "h5a_get_name", "h5a_get_space",
            "h5a_get_type", "h5a_get_num_attrs", "h5a_read", "h5g_open", "rk4", "voigt"
    ));

    static boolean foundlibs = false;

    public void findlibs( String path )
    {
        if (path.startsWith("+")) path = path.substring(1);
        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) findlibs( f.getAbsolutePath() );
            else
            {
                Path file = Paths.get(f.getPath());
                try {
                    InputStream in = Files.newInputStream(file);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    String filename = f.getName();
                    if (filename.substring(filename.length() - 4).equals(".pro")) {
                        while ((line = reader.readLine()) != null) {
                            if (line.length() >= 3 && line.substring(0, 3).toLowerCase().equals("pro"))
                            {
                                GDL_SYSTEM_PRODEDURES.add(filename.substring(0, filename.length() - 4));
                                break;
                            }
                            else if (line.length() >= 8 && line.substring(0, 8).toLowerCase().equals("function"))
                            {
                                GDL_SYSTEM_FUNCTIONS.add(filename.substring(0, filename.length() - 4));
                                break;
                            }
                        }
                    }
                } catch (IOException x) {
                    System.err.println(x);
                }
            }
        }
    }

    public GDLCodeScanner(GDLColorProvider provider) {
        IToken keyword= new Token(new
                TextAttribute(provider.getColor(GDLColorProvider.KEYWORD), null, SWT.BOLD));
        IToken number= new Token(new
                TextAttribute(provider.getColor(GDLColorProvider.NUMBER), null, SWT.BOLD));
        IToken string= new Token(new
                TextAttribute(provider.getColor(GDLColorProvider.STRING)));
        IToken comment= new Token(new
                TextAttribute(provider.getColor(GDLColorProvider.COMMENT)));
        IToken structurefield= new Token(new
                TextAttribute(provider.getColor(GDLColorProvider.TEXT), null, SWT.ITALIC));
        IToken systemprocedure= new Token(new
                TextAttribute(provider.getColor(GDLColorProvider.SYSTEMPRODECURE), null, SWT.BOLD));
        IToken systemfunction= new Token(new
                TextAttribute(provider.getColor(GDLColorProvider.SYSTEMFUNCTION), null, SWT.BOLD));
        IToken other= new Token(new
                TextAttribute(provider.getColor(GDLColorProvider.TEXT)));

        List<IRule> rules= new ArrayList<IRule>();

        // Add procedures/functions list from GDL_PATH
        if (!foundlibs)
        {
            String gdl_path =  System.getenv().get("GDL_PATH");
            if (gdl_path == null) {
                gdl_path = "/usr/local/share/gnudatalanguage/lib";
            }
            findlibs(gdl_path);
            foundlibs = true;
        }

        // Add rule for whitespaces.
        rules.add(new WhitespaceRule(new GDLWhitespaceDetector()));

        // Add rule for comments.
        rules.add(new EndOfLineRule(";", comment));

        // Add rule for strings.
        rules.add(new SingleLineRule("\"", "\"", string, '\\'));
        rules.add(new SingleLineRule("'", "'", string, '\\'));

        // Add rule for keywords, system functions, system procedures
        WordRule wordRule = new WordRule(new GDLWordDetector(), other, true);
        for (String word: GDL_KEYWORDS)
            wordRule.addWord(word, keyword);
        for (String word: GDL_SYSTEM_PRODEDURES)
            wordRule.addWord(word, systemprocedure);
        for (String word: GDL_SYSTEM_FUNCTIONS)
            wordRule.addWord(word, systemfunction);
        rules.add(wordRule);

        // Add rule for numbers
        rules.add(new GDLNumberRule(number));

        // Add rule for structure field
        rules.add(new GDLStructureFieldRule(structurefield));

        IRule[] result= new IRule[rules.size()];
        rules.toArray(result);
        setRules(result);

    }
}